package com.ua.erent.module.core.presentation.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.ua.erent.R;
import com.ua.erent.module.core.di.target.InjectableActivity;
import com.ua.erent.module.core.presentation.mvp.component.ItemsComponent;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IItemsPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.ItemModel;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.IItemsView;
import com.ua.erent.module.core.presentation.mvp.view.util.IFutureBitmap;
import com.ua.erent.module.core.presentation.mvp.view.util.IUrlFutureBitmap;
import com.ua.erent.module.core.presentation.mvp.view.util.ImageUtils;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;

/**
 * <p>
 * Items screen
 * </p>
 * Created by Максим on 11/13/2016.
 */

public final class ItemsActivity extends InjectableActivity<ItemsActivity, IItemsPresenter>
        implements IItemsView {

    private static final String TAG = ItemsActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.items_container)
    protected RecyclerView itemsRecycleView;

    @BindView(R.id.items_swipe_refresh)
    protected SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.items_fab_menu)
    protected FloatingActionMenu floatingActionMenu;

    @BindView(R.id.items_info_title)
    protected TextView infoTextView;

    private final LinearLayoutManager layoutManager;

    private class Adapter extends RecyclerView.Adapter<TypedViewHolder> {

        private final List<RecyclerItem> data;

        Adapter() {
            data = new ArrayList<>(15);
            setHasStableIds(true);
        }

        @Override
        public int getItemViewType(int position) {
            return data.get(position).getType().getValId();
        }

        @Override
        public long getItemId(int position) {
            return data.get(position).getId();
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public TypedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            final ContentType type = ContentType.forId(viewType);
            final int rootId = type == ContentType.CONTENT ? R.layout.good_item : R.layout.progress_item;

            return new ItemHolder(LayoutInflater.from(getContext()).inflate(rootId, parent, false), type);
        }

        @Override
        public void onBindViewHolder(TypedViewHolder mHolder, int position) {

            if (mHolder.getType() == ContentType.CONTENT) {

                final ItemModel model = data.get(position).getPayload();
                final ItemHolder holder = (ItemHolder) mHolder;

                holder.setId(model.getId());
                holder.setAvatarBm(model.getUserAvatar());

                if (!model.getGallery().isEmpty()) {
                    holder.setPhotoBm(model.getGallery().iterator().next());
                }

                holder.getTitle().setText(model.getTitle());
                holder.getSubTitle().setText(getContext().getString(R.string.items_sub_title,
                        model.getRegion(),
                        model.getPrice(),
                        model.getUsername()));

                holder.getDescription().setText(model.getDescription());
                holder.getTitle().setOnClickListener(v -> presenter.onItemClicked(model.getId()));
                holder.getDescription().setOnClickListener(v -> presenter.onItemClicked(model.getId()));
                holder.getActionMenu().setOnClickListener(v -> showItemPopup(v, model.getId(), model.getUserId()));
            }
        }

        @Override
        public void onViewAttachedToWindow(TypedViewHolder mHolder) {

            if (mHolder.getType() == ContentType.CONTENT) {

                final ItemHolder holder = (ItemHolder) mHolder;

                if (holder.getAvatarBm() != null) {
                    loadInto(holder.getAvatarBm(), holder.getAvatar(), R.drawable.ic_account_circle_def_24dp);
                }

                if (holder.getPhotoBm() != null) {
                    holder.getPhoto().setVisibility(View.VISIBLE);
                    holder.getPhoto().setOnClickListener(v -> presenter.onPhotoClicked(holder.getId(), holder.getPhoto()));
                    loadInto(holder.getPhotoBm(), holder.getPhoto(), R.drawable.image_placeholder_photo);
                }
            }
        }

        @Override
        public void onViewDetachedFromWindow(TypedViewHolder mHolder) {
            if (mHolder.getType() == ContentType.CONTENT) {

                final ItemHolder holder = (ItemHolder) mHolder;

                holder.setPhotoBm(null);
                holder.setAvatarBm(null);
                holder.getAvatar().setImageResource(R.drawable.ic_account_circle_def_24dp);
                holder.getPhoto().setImageDrawable(null);
                holder.getPhoto().setVisibility(View.GONE);
            }
        }

        void setItems(@NotNull Collection<ItemModel> items) {
            data.clear();
            data.addAll(fromModels(items));
        }

        void addBegin(@NotNull Collection<ItemModel> items) {
            data.addAll(0, fromModels(items));
        }

        void addEnd(@NotNull Collection<ItemModel> items) {
            data.addAll(fromModels(items));
        }

        void addLoaderStart() {

            if (data.isEmpty() || data.get(0).getType() != ContentType.LOADER) {
                data.add(0, new RecyclerItem(ContentType.LOADER, null));
            }
        }

        boolean removeLoaderStart() {

            if (!data.isEmpty() && data.get(0).getType() == ContentType.LOADER) {
                data.remove(0);
                return true;
            }

            return false;
        }

        void addLoaderEnd() {

            final int lastIndx = data.size() - 1;

            if (data.isEmpty() || data.get(lastIndx).getType() != ContentType.LOADER) {
                data.add(new RecyclerItem(ContentType.LOADER, null));
            }
        }

        boolean removeLoaderEnd() {

            final int lastIndx = data.size() - 1;

            if (!data.isEmpty() && data.get(lastIndx).getType() == ContentType.LOADER) {
                data.remove(lastIndx);
                return true;
            }

            return false;
        }

        private Collection<RecyclerItem> fromModels(Collection<ItemModel> itemModels) {
            final Collection<RecyclerItem> result = new ArrayList<>(itemModels.size());
            for (final ItemModel itemModel : itemModels) {
                result.add(new RecyclerItem(ContentType.CONTENT, itemModel));
            }
            return result;
        }

        private void loadInto(IFutureBitmap futureBitmap, ImageView view, @DrawableRes int defaultId) {
            final ViewGroup.LayoutParams params = view.getLayoutParams();
            final WeakReference<ImageView> avatarRef = new WeakReference<>(view);

            futureBitmap.fetch(params.width, params.height, ItemsActivity.this)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            bm -> {
                                if (avatarRef.get() != null)
                                    view.setImageBitmap(bm);
                            },
                            th -> {
                                if (avatarRef.get() != null)
                                    view.setImageResource(defaultId);
                            });
        }

    }

    private class ScrollListener extends RecyclerView.OnScrollListener {

        private final int hideDelta;
        private final int showDelta;
        private int deltaY;

        ScrollListener(int hideDelta, int showDelta) {
            this.hideDelta = hideDelta;
            this.showDelta = showDelta;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            hideKeyboard();

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                final int firstPos = layoutManager.findFirstVisibleItemPosition();
                final int lastPos = layoutManager.findLastVisibleItemPosition();

                if (lastPos - firstPos >= adapter.getItemCount() || deltaY == 0) {
                    swipeRefreshLayout.setEnabled(true);

                } else if (firstPos == 0 && deltaY < 0) {
                    swipeRefreshLayout.setEnabled(false);
                    adapter.addLoaderStart();
                    adapter.notifyItemInserted(0);
                    layoutManager.scrollToPosition(0);
                    presenter.onLoadNext();

                } else if (lastPos == adapter.getItemCount() - 1 && deltaY > 0) {
                    swipeRefreshLayout.setEnabled(false);
                    adapter.addLoaderEnd();
                    adapter.notifyItemInserted(lastPos + 1);
                    layoutManager.scrollToPosition(lastPos + 1);
                    presenter.onLoadPrev();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            deltaY = dy;

            if (dy >= hideDelta) {
                floatingActionMenu.hideMenuButton(true);
            } else if (-dy >= showDelta) {
                floatingActionMenu.showMenuButton(true);
            }
        }
    }

    private final Adapter adapter;

    public ItemsActivity() {
        super(R.layout.activity_items, ItemsComponent.class);
        adapter = new Adapter();
        layoutManager = new LinearLayoutManager(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window window = getWindow();

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            final Transition transition = TransitionInflater.from(this)
                    .inflateTransition(android.R.transition.move);

            window.setSharedElementEnterTransition(transition);
            window.setSharedElementExitTransition(transition);
            window.setSharedElementsUseOverlay(false);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.primary));
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.onRefresh());
        itemsRecycleView.setLayoutManager(layoutManager);
        itemsRecycleView.setAdapter(adapter);
        itemsRecycleView.addOnScrollListener(new ScrollListener(ImageUtils.dpToPx(5), ImageUtils.dpToPx(5)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.items_menu, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        final EditText editTextView = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        try {
            final Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(editTextView, R.drawable.light_cursor);
        } catch (final Exception e) {
            Log.e(TAG, "onCreateOptionsMenu: failed to get cursor", e);
        }

        searchView.setQueryHint(getString(R.string.items_search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.onSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }

        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int id = item.getItemId();

        if (id == android.R.id.home) {
            presenter.onBackButton();
        } else if (id == R.id.action_refresh) {
            presenter.onRefresh();
        }

        return true;
    }

    @NotNull
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setTitle(@NotNull String title) {
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public void showGallery(Collection<? extends IUrlFutureBitmap> futureBitmaps, ImageView imageView) {

        final Intent intent = new Intent(this, GalleryActivity.class);

        intent.putParcelableArrayListExtra(GalleryActivity.ARG_IMAGES, new ArrayList<>(futureBitmaps));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // start smooth transition between activities
            final ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this, imageView, imageView.getTransitionName());
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void showMessage(@NotNull String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressStart() {
        adapter.addLoaderStart();
        adapter.notifyItemInserted(0);
    }

    @Override
    public void hideProgressStart() {
        adapter.removeLoaderStart();
        adapter.notifyItemRemoved(0);
    }

    @Override
    public void showProgressEnd() {
        adapter.addLoaderEnd();
        adapter.notifyItemInserted(adapter.getItemCount() - 1);
    }

    @Override
    public void hideProgressEnd() {
        adapter.removeLoaderEnd();
        adapter.notifyItemRemoved(adapter.getItemCount() - 1);
    }

    @Override
    public void showProgress() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setItems(@NotNull Collection<ItemModel> items) {
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addNextItems(@NotNull Collection<ItemModel> items) {

        final boolean removed = adapter.removeLoaderStart();

        if (removed) {
            adapter.notifyItemRemoved(0);
        }
        adapter.addBegin(items);
        adapter.notifyItemRangeInserted(0, items.size());
    }

    @Override
    public void addPrevItems(@NotNull Collection<ItemModel> items) {
        int lastPos = adapter.getItemCount() - 1;
        final boolean removed = adapter.removeLoaderEnd();

        if (removed) {
            adapter.notifyItemRemoved(lastPos);
            lastPos = adapter.getItemCount() - 1;
        }
        adapter.addEnd(items);
        adapter.notifyItemRangeInserted(lastPos, items.size());
    }

    @Override
    public void showText(@NotNull String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setInfoMessageVisible(boolean visible) {
        infoTextView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setInfoMessage(String text) {
        infoTextView.setText(text);
    }

    @Override
    public void hideKeyboard() {

        final View view = getCurrentFocus();

        if (view != null) {

            final InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        presenter.onBackButton();
    }

    private void showItemPopup(View v, long itemId, long userId) {

        final PopupMenu popupMenu = new PopupMenu(this, v);

        popupMenu.inflate(presenter.getPopupResId());
        popupMenu.setOnMenuItemClickListener(item -> {

            final int id = item.getItemId();

            if (id == R.id.action_email) {
                presenter.onComplain(itemId);
            } else if (id == R.id.action_conversation) {
                presenter.onOpenDialog(itemId, userId);
            }

            return true;
        });
        popupMenu.show();
    }

}
