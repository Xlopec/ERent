package com.ua.erent.module.core.presentation.mvp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ua.erent.R;
import com.ua.erent.module.core.di.target.InjectableActivity;
import com.ua.erent.module.core.presentation.mvp.component.CategoriesComponent;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ICategoriesPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.ICategoriesView;
import com.ua.erent.module.core.presentation.mvp.view.util.IFutureBitmap;
import com.ua.erent.module.core.presentation.mvp.view.util.ImageUtils;
import com.ua.erent.module.core.presentation.mvp.view.util.SpaceDecorator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class CategoriesActivity extends InjectableActivity<CategoriesActivity, ICategoriesPresenter>
        implements ICategoriesView, NavigationView.OnNavigationItemSelectedListener,
        DrawerLayout.DrawerListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.drawer_layout_main)
    protected DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    protected NavigationView navigationView;

    @BindView(R.id.category_container)
    protected RecyclerView categoriesRecycleView;

    @BindView(R.id.category_swipe_refresh)
    protected SwipeRefreshLayout refreshLayout;

    private ImageView bgHeaderImageView;
    private TextView headerCredentials;
    private TextView headerEmail;
    private View headerContainer;

    private ActionBarDrawerToggle toggle;

    private final Adapter adapter;

    private static final class CategoryHolder extends RecyclerView.ViewHolder {

        private final ImageView categoryPhoto;
        private final TextView categoryTitle;
        private final TextView categoryDescription;
        private IFutureBitmap bitmap;

        CategoryHolder(View itemView) {
            super(itemView);
            categoryPhoto = (ImageView) itemView.findViewById(R.id.category_photo);
            categoryTitle = (TextView) itemView.findViewById(R.id.category_title);
            categoryDescription = (TextView) itemView.findViewById(R.id.category_description);
        }

        void setBitmap(IFutureBitmap bitmap) {
            this.bitmap = bitmap;
        }

        void setCategoryTitle(String title) {
            categoryTitle.setText(title);
        }

        void setDescription(String description) {
            categoryDescription.setText(description);
        }

        void tryLoadImage(int color, Context context) {

            final ViewGroup.LayoutParams params = categoryPhoto.getLayoutParams();

            bitmap.fetch(params.width, params.height, context)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(categoryPhoto::setImageBitmap,
                            th -> categoryPhoto.setBackgroundColor(color));
        }

    }

    private class Adapter extends RecyclerView.Adapter<CategoryHolder> {

        private final ArrayList<CategoryModel> content;

        Adapter() {
            content = new ArrayList<>();
        }

        @Override
        public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CategoryHolder(LayoutInflater.from(getContext()).inflate(R.layout.category_item, parent, false));
        }

        @Override
        public void onBindViewHolder(CategoryHolder holder, int position) {

            final CategoryModel model = content.get(position);

            holder.setBitmap(model.getFutureBitmap());
            holder.setCategoryTitle(model.getTitle());
            holder.setDescription(model.getDescription());
            holder.itemView.setOnClickListener(v -> presenter.onOpenCategory(model.getId()));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return content.size();
        }

        @Override
        public void onViewAttachedToWindow(CategoryHolder holder) {
            holder.tryLoadImage(presenter.getRandomColor(), CategoriesActivity.this);
        }

        void addItem(CategoryModel model) {
            content.add(model);
        }

        void clearItems() {
            content.clear();
        }

        void addItem(@NotNull Collection<CategoryModel> models) {
            content.addAll(models);
        }

    }

    public CategoriesActivity() {
        super(R.layout.activity_main_drawer_layout, CategoriesComponent.class);
        adapter = new Adapter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final Window w = getWindow();
            //w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.primary));
        refreshLayout.setOnRefreshListener(() -> presenter.onRefresh());

        final SpaceDecorator decorator = new SpaceDecorator(
                ImageUtils.dpToPx(categoriesRecycleView.getPaddingLeft() / 2));

        categoriesRecycleView.addItemDecoration(decorator);
        categoriesRecycleView.setLayoutManager(new GridLayoutManager(this, 2));
        categoriesRecycleView.setAdapter(adapter);

        final View headerView = navigationView.getHeaderView(0);

        headerContainer = headerView.findViewById(R.id.nav_header_container);
        headerCredentials = (TextView) headerView.findViewById(R.id.nav_username);
        headerEmail = (TextView) headerView.findViewById(R.id.nav_email);
        bgHeaderImageView = (ImageView) headerView.findViewById(R.id.nav_bg);
        bgHeaderImageView.setDrawingCacheEnabled(true);
        bgHeaderImageView.buildDrawingCache();
        headerContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                headerContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                setNavImageAsync();
                return true;
            }
        });

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.main_drawer_open, R.string.main_drawer_close);

        drawerLayout.addDrawerListener(this);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.categories_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        final int id = item.getItemId();

        if (id == R.id.action_refresh) {
            refreshLayout.setRefreshing(true);
            presenter.onRefresh();
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {

        final int id = item.getItemId();

        if (id == R.id.action_profile) {

        } else if (id == R.id.action_conversation) {

        } else if (id == R.id.action_items) {

        } else if (id == R.id.action_logout) {
            presenter.onLogout();
        } else if (id == R.id.action_setting) {

        } else if (id == R.id.action_login) {
            presenter.onLogin();
        }

        return true;
    }

    @NotNull
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void hideRefreshProgress() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showRefreshProgress() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

        if (bgHeaderImageView != null && bgHeaderImageView.getDrawable() == null) {

            final Bitmap cachedBitmap = bgHeaderImageView.getDrawingCache();

            if (cachedBitmap == null) {
                setNavImageAsync();
            } else {
                bgHeaderImageView.setImageBitmap(cachedBitmap);
            }
        }
    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public void setDrawerMenu(@MenuRes int resId) {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(resId);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void setCredentials(String fullName, String email) {

        headerCredentials.setVisibility(fullName == null ? View.GONE : View.VISIBLE);
        headerEmail.setVisibility(email == null ? View.GONE : View.VISIBLE);

        headerCredentials.setText(fullName);
        headerEmail.setText(email);
    }

    @Override
    public void showMessage(@NotNull String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addCategory(@NotNull CategoryModel model) {
        adapter.addItem(model);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addCategory(@NotNull Collection<CategoryModel> models) {
        adapter.addItem(models);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void clearCategories() {
        adapter.clearItems();
        adapter.notifyDataSetChanged();
    }

    private void setNavImageAsync() {

        final ViewGroup.LayoutParams params = headerContainer.getLayoutParams();

        Observable.create(new Observable.OnSubscribe<BitmapDrawable>() {
            @Override
            public void call(Subscriber<? super BitmapDrawable> subscriber) {
                final Bitmap bitmap = ImageUtils.decodeSampledBitmapFromResource(
                        getResources(),
                        R.drawable.nav_drawer_bg,
                        ImageUtils.pxToDp(params.width),
                        ImageUtils.pxToDp(params.height)
                );
                subscriber.onNext(new BitmapDrawable(getResources(), bitmap));
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bgHeaderImageView::setImageDrawable);
    }

}
