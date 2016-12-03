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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.ua.erent.module.core.presentation.mvp.view.util.IParcelableFutureBitmap;
import com.ua.erent.module.core.presentation.mvp.view.util.ImageUtils;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import dagger.internal.Preconditions;
import rx.android.schedulers.AndroidSchedulers;

/**
 * <p>
 * Items screen
 * </p>
 * Created by Максим on 11/13/2016.
 */

public final class ItemsActivity extends InjectableActivity<ItemsActivity, IItemsPresenter>
        implements IItemsView {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.items_container)
    protected RecyclerView itemsRecycleView;

    @BindView(R.id.items_swipe_refresh)
    protected SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.items_fab_menu)
    protected FloatingActionMenu floatingActionMenu;

    private final LinearLayoutManager layoutManager;

    public enum Type {

        LOADER(0), CONTENT(1);

        private final int type;

        Type(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public static Type forId(int id) {
            for (final Type t : Type.values()) {
                if (t.type == id) return t;
            }
            return null;
        }
    }

    private abstract static class TypedViewHolder extends RecyclerView.ViewHolder {

        private final Type type;

        TypedViewHolder(@NotNull View itemView, @NotNull Type type) {
            super(itemView);
            this.type = Preconditions.checkNotNull(type);
        }

        public Type getType() {
            return type;
        }
    }

    private static class ItemHolder extends TypedViewHolder {

        private long id;
        private final ImageView avatar;
        private final ImageView photo;
        private final TextView title;
        private final TextView subTitle;
        private final TextView description;

        private IFutureBitmap avatarBm;
        private IFutureBitmap photoBm;

        ItemHolder(View itemView, Type type) {
            super(itemView, type);

            if (type == Type.CONTENT) {
                avatar = (ImageView) itemView.findViewById(R.id.item_user_avatar);
                title = (TextView) itemView.findViewById(R.id.item_title);
                subTitle = (TextView) itemView.findViewById(R.id.item_sub_title);
                description = (TextView) itemView.findViewById(R.id.item_description);
                photo = (ImageView) itemView.findViewById(R.id.item_photo);
            } else {
                avatar = photo = null;
                title = subTitle = description = null;
            }
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }
    }

    private static class RecyclerItem {

        private static long gen;

        private final long id;
        private final Type type;
        private final Object payload;

        RecyclerItem(Type type, Object payload) {
            this.id = gen++;
            this.type = type;
            this.payload = payload;
        }

        public long getId() {
            return id;
        }

        public Type getType() {
            return type;
        }

        @SuppressWarnings("unchecked")
        <T> T getPayload() {
            return (T) payload;
        }
    }

    private class Adapter extends RecyclerView.Adapter<TypedViewHolder> {

        private final List<RecyclerItem> data;

        Adapter() {
            data = new ArrayList<>(15);
            setHasStableIds(true);
        }

        @Override
        public int getItemViewType(int position) {
            return data.get(position).getType().getType();
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

            final Type type = Type.forId(viewType);
            final int rootId = type == Type.CONTENT ? R.layout.good_item : R.layout.progress_item;

            return new ItemHolder(LayoutInflater.from(getContext()).inflate(rootId, parent, false), type);
        }

        @Override
        public void onBindViewHolder(TypedViewHolder mHolder, int position) {

            if (mHolder.type == Type.CONTENT) {

                final ItemModel model = data.get(position).getPayload();
                final ItemHolder holder = (ItemHolder) mHolder;

                holder.setId(model.getId());
                holder.avatarBm = model.getUserAvatar();

                if (!model.getGallery().isEmpty()) {
                    holder.photoBm = model.getGallery().iterator().next();
                }

                holder.title.setText(model.getTitle());
                holder.subTitle.setText(getContext().getString(R.string.items_sub_title,
                        model.getRegion(),
                        model.getPrice(),
                        model.getUsername()));

                holder.description.setText(model.getDescription());
            }
        }

        @Override
        public void onViewAttachedToWindow(TypedViewHolder mHolder) {

            if (mHolder.type == Type.CONTENT) {

                final ItemHolder holder = (ItemHolder) mHolder;

                if (holder.avatarBm != null) {
                    loadInto(holder.avatarBm, holder.avatar, R.drawable.ic_account_circle_def_24dp);
                }

                if (holder.photoBm != null) {
                    holder.photo.setVisibility(View.VISIBLE);
                    holder.photo.setOnClickListener(v -> presenter.onPhotoClicked(holder.getId(), holder.photo));
                    loadInto(holder.photoBm, holder.photo, R.drawable.image_placeholder_photo);
                }
            }
        }

        @Override
        public void onViewDetachedFromWindow(TypedViewHolder mHolder) {
            if (mHolder.type == Type.CONTENT) {

                final ItemHolder holder = (ItemHolder) mHolder;

                holder.photoBm = null;
                holder.avatarBm = null;
                holder.avatar.setImageResource(R.drawable.ic_account_circle_def_24dp);
                holder.photo.setImageDrawable(null);
                holder.photo.setVisibility(View.GONE);
            }
        }

        void addBegin(@NotNull Collection<ItemModel> items) {
            data.addAll(0, fromModels(items));
        }

        void addEnd(@NotNull Collection<ItemModel> items) {
            data.addAll(fromModels(items));
        }

        void addLoaderStart() {

            if (data.isEmpty() || data.get(0).getType() != Type.LOADER) {
                data.add(0, new RecyclerItem(Type.LOADER, null));
            }
        }

        void removeLoaderStart() {

            if (!data.isEmpty() && data.get(0).getType() == Type.LOADER) {
                data.remove(0);
            }
        }

        void addLoaderEnd() {

            final int lastIndx = data.size() - 1;

            if (data.isEmpty() || data.get(lastIndx).getType() != Type.LOADER) {
                data.add(new RecyclerItem(Type.LOADER, null));
            }
        }

        void removeLoaderEnd() {

            final int lastIndx = data.size() - 1;

            if (!data.isEmpty() && data.get(lastIndx).getType() == Type.LOADER) {
                data.remove(lastIndx);
            }
        }

        private Collection<RecyclerItem> fromModels(Collection<ItemModel> itemModels) {
            final Collection<RecyclerItem> result = new ArrayList<>(itemModels.size());
            for (final ItemModel itemModel : itemModels) {
                result.add(new RecyclerItem(Type.CONTENT, itemModel));
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

        ScrollListener(int hideDelta, int showDelta) {
            this.hideDelta = hideDelta;
            this.showDelta = showDelta;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                final int firstPos = layoutManager.findFirstVisibleItemPosition();
                final int lastPos = layoutManager.findLastVisibleItemPosition();

                if (lastPos - firstPos >= adapter.getItemCount()) {
                    swipeRefreshLayout.setEnabled(true);

                } else if (firstPos == 0) {
                    swipeRefreshLayout.setEnabled(false);
                    adapter.addLoaderStart();
                    adapter.notifyItemInserted(0);
                    layoutManager.scrollToPosition(0);
                    presenter.onLoadNext();

                } else if (lastPos == adapter.getItemCount() - 1) {
                    swipeRefreshLayout.setEnabled(false);
                    adapter.addLoaderEnd();
                    adapter.notifyDataSetChanged();//ItemInserted(adapter.getItemCount() - 1);
                    //  layoutManager.scrollToPosition(adapter.getItemCount() - 1);
                    presenter.onLoadPrev();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

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
            window.setSharedElementEnterTransition(TransitionInflater.from(this)
                    .inflateTransition(android.R.transition.move));
        }

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.primary));
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.onRefresh());
        itemsRecycleView.setLayoutManager(layoutManager);
        itemsRecycleView.setAdapter(adapter);
        itemsRecycleView.addOnScrollListener(new ScrollListener(ImageUtils.dpToPx(5), ImageUtils.dpToPx(5)));
    }

    @NotNull
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showGallery(Collection<? extends IParcelableFutureBitmap> futureBitmaps, ImageView imageView) {

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
        adapter.notifyDataSetChanged();//ItemInserted(0);
    }

    @Override
    public void hideProgressStart() {
        adapter.removeLoaderStart();
        adapter.notifyDataSetChanged();//ItemRemoved(0);
    }

    @Override
    public void showProgressEnd() {
        adapter.addLoaderEnd();
        adapter.notifyDataSetChanged();//ItemInserted(adapter.getItemCount() - 1);
    }

    @Override
    public void hideProgressEnd() {
        adapter.removeLoaderEnd();
        adapter.notifyDataSetChanged();//ItemRemoved(adapter.getItemCount() - 1);
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
    public void addNextItems(@NotNull Collection<ItemModel> items) {
        adapter.removeLoaderStart();

        if (!items.isEmpty()) {
            adapter.addBegin(items);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addPrevItems(@NotNull Collection<ItemModel> items) {
        adapter.removeLoaderEnd();

        if (!items.isEmpty()) {
            adapter.addEnd(items);
        }
        adapter.notifyDataSetChanged();
    }
}
