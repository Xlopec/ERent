package com.ua.erent.module.core.presentation.mvp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ua.erent.R;
import com.ua.erent.module.core.di.target.InjectableActivity;
import com.ua.erent.module.core.presentation.mvp.component.ItemsComponent;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IItemsPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.ItemModel;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.IItemsView;
import com.ua.erent.module.core.presentation.mvp.view.util.IFutureBitmap;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Максим on 11/13/2016.
 */

public final class ItemsActivity extends InjectableActivity<ItemsActivity, IItemsPresenter>
        implements IItemsView {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.items_container)
    protected RecyclerView itemsRecycleView;

    private static class ItemHolder extends RecyclerView.ViewHolder {

        private final ImageView avatar;
        private final ImageView photo;
        private final TextView title;
        private final TextView subTitle;
        private final TextView description;

        private IFutureBitmap avatarBm;
        private IFutureBitmap photoBm;

        ItemHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.item_user_avatar);
            title = (TextView) itemView.findViewById(R.id.item_title);
            subTitle = (TextView) itemView.findViewById(R.id.item_sub_title);
            description = (TextView) itemView.findViewById(R.id.item_description);
            photo = (ImageView) itemView.findViewById(R.id.item_photo);
        }

    }

    private class Adapter extends RecyclerView.Adapter<ItemHolder> {

        private final List<ItemModel> data;

        Adapter() {
            data = new ArrayList<>(15);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemHolder(LayoutInflater.from(getContext()).inflate(R.layout.good_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {

            final ItemModel model = data.get(position);

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

        @Override
        public void onViewAttachedToWindow(ItemHolder holder) {

            if (holder.avatarBm != null) {
                loadInto(holder.avatarBm, holder.avatar);
            }

            if (holder.photoBm != null) {
                loadInto(holder.photoBm, holder.photo);
            }
        }

        void addBegin(@NotNull Collection<ItemModel> items) {
            data.addAll(0, items);
        }

        void addEnd(@NotNull Collection<ItemModel> items) {
            data.addAll(items);
        }

        private void loadInto(IFutureBitmap futureBitmap, ImageView view) {
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
                                    view.setImageResource(R.drawable.ic_account_circle_blue_24dp);
                            });
        }

    }

    private final Adapter adapter;

    public ItemsActivity() {
        super(R.layout.activity_items, ItemsComponent.class);
        adapter = new Adapter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        itemsRecycleView.setLayoutManager(new LinearLayoutManager(this));
       // itemsRecycleView.addItemDecoration(new SimpleDividerItemDecoration(this, R.drawable.divider));
        itemsRecycleView.setAdapter(adapter);
    }

    @NotNull
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showMessage(@NotNull String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressStart() {

    }

    @Override
    public void hideProgressStart() {

    }

    @Override
    public void showProgressEnd() {

    }

    @Override
    public void hideProgressEnd() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void addNextItems(@NotNull Collection<ItemModel> items) {
        adapter.addBegin(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addPrevItems(@NotNull Collection<ItemModel> items) {
        adapter.addEnd(items);
        adapter.notifyDataSetChanged();
    }
}
