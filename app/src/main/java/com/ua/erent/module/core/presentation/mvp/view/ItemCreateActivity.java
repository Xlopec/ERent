package com.ua.erent.module.core.presentation.mvp.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.ua.erent.R;
import com.ua.erent.module.core.di.target.InjectableActivity;
import com.ua.erent.module.core.presentation.mvp.component.ItemCreationComponent;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ItemCreationPresenter;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.ItemCreationView;
import com.ua.erent.module.core.presentation.mvp.view.util.IFutureBitmap;
import com.ua.erent.module.core.presentation.mvp.view.util.UriBitmap;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import dagger.internal.Preconditions;
import rx.android.schedulers.AndroidSchedulers;

public final class ItemCreateActivity extends InjectableActivity<ItemCreateActivity, ItemCreationPresenter>
        implements ItemCreationView, Validator.ValidationListener {

    private static final String ARG_CATEGORY_CACHE = "argCategoryCache";
    private static final String ARG_BRAND_CACHE = "argBrandCache";
    private static final String ARG_REGION_CACHE = "argRegionCache";
    private static final String ARG_IMAGE_CACHE = "argImageCache";

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.item_name)
    @Length(min = 3, max = 128)
    protected EditText nameEditText;

    @BindView(R.id.item_price)
    @NotEmpty
    protected EditText priceEditText;

    @BindView(R.id.item_category_root)
    protected View categoryHolder;

    @BindView(R.id.item_category)
    protected TextView categoryTextView;

    @BindView(R.id.item_brand_root)
    protected View brandHolder;

    @BindView(R.id.item_brand)
    protected TextView brandTextView;

    @BindView(R.id.item_region_root)
    protected View regionHolder;

    @BindView(R.id.item_region)
    protected TextView regionTextView;

    @BindView(R.id.item_description)
    @Length(min = 10, max = 1024)
    protected EditText descriptionEditText;

    @BindView(R.id.image_view_pager)
    protected ViewPager imagePager;

    @BindView(R.id.toolbar_layout)
    protected CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.fab)
    protected FloatingActionButton fab;

    private class Adapter extends PagerAdapter {

        private final ArrayList<UriBitmap> images;

        Adapter() {
            this.images = new ArrayList<>(0);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            final View view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.create_image_item, container, false);

            final UriBitmap futureBitmap = images.get(position);
            final ImageView photoView = (ImageView) view.findViewById(R.id.image);
            final WeakReference<ImageView> reference = new WeakReference<>(photoView);

            view.setOnClickListener(v -> presenter.onSelectImage());
            photoView.setOnLongClickListener(v -> {
                presenter.onDeleteImage(futureBitmap.getUri());
                images.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            });
            container.addView(view);
            photoView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {

                    photoView.getViewTreeObserver().removeOnPreDrawListener(this);
                    futureBitmap.fetch(photoView.getWidth(), photoView.getHeight(), ItemCreateActivity.this)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    bm -> {
                                        if (reference.get() != null)
                                            photoView.setImageBitmap(bm);
                                    },
                                    th -> {
                                        if (reference.get() != null)
                                            photoView.setImageResource(R.drawable.image_placeholder_photo);
                                    });

                    return true;
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        IFutureBitmap getImage(int index) {
            return images.get(index);
        }

        void addAll(Collection<? extends UriBitmap> bitmaps) {
            images.addAll(Preconditions.checkNotNull(bitmaps));
        }

        void add(UriBitmap bitmap) {
            images.add(Preconditions.checkNotNull(bitmap));
        }

        void onSaveInstanceState(Bundle out) {
            out.putParcelableArrayList(ARG_IMAGE_CACHE, images);
        }

        void onRestoreInstanceState(Bundle savedState) {
            final ArrayList<UriBitmap> cache =
                    savedState.getParcelableArrayList(ARG_IMAGE_CACHE);

            if (cache != null) {
                images.addAll(cache);
            }
        }

        void removeAll() {
            images.clear();
        }
    }

    private final Adapter adapter;
    private final Validator validator;
    private ProgressDialog progressDialog;

    public ItemCreateActivity() {
        super(R.layout.activity_item_create, ItemCreationComponent.class);
        validator = new Validator(this);
        validator.setValidationListener(this);
        adapter = new Adapter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        progressDialog = new ProgressDialog(this, R.style.DefaultDialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        categoryHolder.setOnClickListener(v -> {
            final PopupMenu menu = createPopupMenu(categoryTextView, presenter.onGetCategories());

            menu.setOnMenuItemClickListener(item -> {
                categoryTextView.setText(item.getTitle());
                presenter.onSetCategory(item.getItemId());
                return true;
            });
            menu.show();
        });

        brandHolder.setOnClickListener(v -> {

            final PopupMenu menu = createPopupMenu(brandTextView, presenter.onGetBrands());

            menu.setOnMenuItemClickListener(item -> {
                brandTextView.setText(item.getTitle());
                presenter.onSetBrand(item.getItemId());
                return true;
            });
            menu.show();
        });

        regionHolder.setOnClickListener(v -> {

            final PopupMenu menu = createPopupMenu(regionTextView, presenter.onGetRegions());

            menu.setOnMenuItemClickListener(item -> {
                regionTextView.setText(item.getTitle());
                presenter.onSetRegion(item.getItemId());
                return true;
            });
            menu.show();
        });

        fab.setOnClickListener(v -> validator.validate());
        imagePager.setAdapter(adapter);
        imagePager.setOnClickListener(v -> presenter.onSelectImage());
        collapsingToolbarLayout.setOnClickListener(v -> presenter.onSelectImage());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        categoryTextView.setText(savedInstanceState.getString(ARG_CATEGORY_CACHE));
        brandTextView.setText(savedInstanceState.getString(ARG_BRAND_CACHE));
        regionTextView.setText(savedInstanceState.getString(ARG_REGION_CACHE));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_create_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @NotNull
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int id = item.getItemId();

        if (id == R.id.action_refresh) {
            presenter.onRefresh();
        } else if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_add_image) {
            presenter.onSelectImage();
        } else if (id == R.id.action_clear_images) {
            presenter.onDeleteImages();
        }

        return true;
    }

    @Override
    public void clearImages() {
        adapter.removeAll();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress(@NotNull String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void addImage(@NotNull UriBitmap bitmap) {
        adapter.add(bitmap);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private PopupMenu createPopupMenu(View v, Collection<SelectableModel> models) {

        final PopupMenu popupMenu = new PopupMenu(this, v);

        for (final SelectableModel selectableModel : models) {
            popupMenu.getMenu().add(0, (int) selectableModel.id, 0, selectableModel.title);
        }

        return popupMenu;
    }

    @Override
    public void onValidationSucceeded() {
        presenter.onCreateItem(nameEditText.getText().toString(),
                descriptionEditText.getText().toString(),
                priceEditText.getText().toString());
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (final ValidationError error : errors) {

            final View view = error.getView();
            final String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(ARG_CATEGORY_CACHE, categoryTextView.getText().toString());
        outState.putString(ARG_BRAND_CACHE, brandTextView.getText().toString());
        outState.putString(ARG_REGION_CACHE, regionTextView.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
