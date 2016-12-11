package com.ua.erent.module.core.presentation.mvp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ua.erent.R;
import com.ua.erent.module.core.di.target.InjectableActivity;
import com.ua.erent.module.core.presentation.mvp.component.MyItemsComponent;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IMyItemsPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.ItemModel;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.IMyItemsView;
import com.ua.erent.module.core.presentation.mvp.view.util.IFutureBitmap;
import com.ua.erent.module.core.presentation.mvp.view.util.IParcelableFutureBitmap;
import com.ua.erent.module.core.presentation.mvp.view.util.ImageUtils;
import com.ua.erent.module.core.presentation.mvp.view.util.SpaceDecorator;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;

public final class MyItemsActivity extends InjectableActivity<MyItemsActivity, IMyItemsPresenter>
        implements IMyItemsView {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.my_items_container)
    protected RecyclerView myItemsRecyclerView;

    @BindView(R.id.info_title)
    protected TextView infoTextView;

    @BindView(R.id.items_swipe_refresh)
    protected SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fab)
    protected FloatingActionButton fab;

    private final LinearLayoutManager layoutManager;
    private final Adapter adapter;

    private static final class LoaderHolder extends TypedViewHolder {

        LoaderHolder(@NotNull View itemView) {
            super(itemView, ContentType.LOADER);
        }
    }

    private static final class ItemHolder extends TypedViewHolder {

        private final ImageView photo;
        private final TextView timestamp;
        private final TextView name;
        private final TextView description;
        private IParcelableFutureBitmap bitmap;

        ItemHolder(View itemView) {
            super(itemView, ContentType.CONTENT);
            photo = (ImageView) itemView.findViewById(R.id.item_photo);
            timestamp = (TextView) itemView.findViewById(R.id.item_timestamp);
            name = (TextView) itemView.findViewById(R.id.item_name);
            description = (TextView) itemView.findViewById(R.id.item_description);
        }

    }

    private class Adapter extends RecyclerView.Adapter<TypedViewHolder> {

        private final ArrayList<RecyclerItem> content;

        Adapter() {
            content = new ArrayList<>();
            setHasStableIds(true);
        }

        @Override
        public int getItemViewType(int position) {
            return content.get(position).getType().getValId();
        }

        @Override
        public TypedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            final ContentType type = ContentType.forId(viewType);
            final LayoutInflater inflater = LayoutInflater.from(MyItemsActivity.this);

            if (type == ContentType.LOADER)
                return new LoaderHolder(inflater.inflate(R.layout.progress_item, parent, false));

            return new ItemHolder(inflater.inflate(R.layout.my_item, parent, false));
        }

        @Override
        public void onBindViewHolder(TypedViewHolder holder, int position) {

            if (holder.getType() == ContentType.CONTENT) {

                final ItemModel model = content.get(position).getPayload();
                final ItemHolder itemHolder = (ItemHolder) holder;

                itemHolder.name.setText(model.getTitle());
                itemHolder.description.setText(model.getDescription());
                itemHolder.timestamp.setText(model.getPubDate());

                if (!model.getGallery().isEmpty()) {
                    itemHolder.bitmap = model.getGallery().iterator().next();
                }
            }
        }

        @Override
        public void onViewAttachedToWindow(TypedViewHolder holder) {

            if (holder.getType() == ContentType.CONTENT) {

                final ItemHolder mHolder = (ItemHolder) holder;

                if (mHolder.bitmap != null) {
                    loadInto(mHolder.bitmap, mHolder.photo, R.drawable.image_placeholder_photo);
                }
            }
        }

        @Override
        public long getItemId(int position) {
            return content.get(position).getId();
        }

        @Override
        public int getItemCount() {
            return content.size();
        }

        void setItems(@NotNull Collection<ItemModel> items) {
            content.clear();
            content.addAll(fromModels(items));
        }

        void addBegin(@NotNull Collection<ItemModel> items) {
            content.addAll(0, fromModels(items));
        }

        void addEnd(@NotNull Collection<ItemModel> items) {
            content.addAll(fromModels(items));
        }

        void addLoaderStart() {

            if (content.isEmpty() || content.get(0).getType() != ContentType.LOADER) {
                content.add(0, new RecyclerItem(ContentType.LOADER, null));
            }
        }

        boolean removeLoaderStart() {

            if (!content.isEmpty() && content.get(0).getType() == ContentType.LOADER) {
                content.remove(0);
                return true;
            }

            return false;
        }

        void addLoaderEnd() {

            final int lastIndx = content.size() - 1;

            if (content.isEmpty() || content.get(lastIndx).getType() != ContentType.LOADER) {
                content.add(new RecyclerItem(ContentType.LOADER, null));
            }
        }

        boolean removeLoaderEnd() {

            final int lastIndx = content.size() - 1;

            if (!content.isEmpty() && content.get(lastIndx).getType() == ContentType.LOADER) {
                content.remove(lastIndx);
                return true;
            }

            return false;
        }

        private void loadInto(IFutureBitmap futureBitmap, ImageView view, @DrawableRes int defaultId) {
            final ViewGroup.LayoutParams params = view.getLayoutParams();
            final WeakReference<ImageView> avatarRef = new WeakReference<>(view);

            futureBitmap.fetch(params.width, params.height, MyItemsActivity.this)
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

        private Collection<RecyclerItem> fromModels(Collection<ItemModel> models) {
            final Collection<RecyclerItem> result = new ArrayList<>(models.size());
            for (final ItemModel model : models) {
                result.add(new RecyclerItem(ContentType.CONTENT, model));
            }
            return result;
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
                fab.hide();
            } else if (-dy >= showDelta) {
                fab.show();
            }
        }
    }

    public MyItemsActivity() {
        super(R.layout.activity_my_items, MyItemsComponent.class);
        adapter = new Adapter();
        layoutManager = new LinearLayoutManager(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final SpaceDecorator decorator = new SpaceDecorator();
        final int padding = ImageUtils.dpToPx(5);//convert to dp later

        decorator.setBottom(padding);
        decorator.setLeft(padding);
        decorator.setRight(padding);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.primary));
        myItemsRecyclerView.setLayoutManager(layoutManager);
        myItemsRecyclerView.setAdapter(adapter);
        myItemsRecyclerView.addItemDecoration(decorator);
        myItemsRecyclerView.addOnScrollListener(new ScrollListener(ImageUtils.dpToPx(5), ImageUtils.dpToPx(5)));
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.onRefresh());
        fab.setOnClickListener(v -> presenter.onCreateNew());
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
    public void setItems(@NotNull Collection<ItemModel> items) {
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addItemsStart(@NotNull Collection<ItemModel> items) {
        final boolean removed = adapter.removeLoaderStart();

        if (removed) {
            adapter.notifyItemRemoved(0);
        }
        adapter.addBegin(items);
        adapter.notifyItemRangeInserted(0, items.size());
    }

    @Override
    public void addItemsEnd(@NotNull Collection<ItemModel> items) {
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
    public void showProgress() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
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
    public void showMessage(@NotNull String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgressStart() {
        adapter.removeLoaderStart();
        adapter.notifyItemRemoved(0);
    }

    @Override
    public void hideProgressEnd() {
        adapter.removeLoaderEnd();
        adapter.notifyItemRemoved(adapter.getItemCount() - 1);
    }

}
