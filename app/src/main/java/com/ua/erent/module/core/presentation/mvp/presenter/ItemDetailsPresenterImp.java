package com.ua.erent.module.core.presentation.mvp.presenter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.widget.ImageView;

import com.ua.erent.R;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ItemDetailsModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ItemDetailsPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.ItemModel;
import com.ua.erent.module.core.presentation.mvp.view.ItemDetailsActivity;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.ItemDetailsView;
import com.ua.erent.module.core.presentation.mvp.view.util.IUrlFutureBitmap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

import javax.inject.Inject;

/**
 * Created by Максим on 12/3/2016.
 */

public final class ItemDetailsPresenterImp extends ItemDetailsPresenter {

    private static final String ARG_CACHE = "argCache";

    private final ItemDetailsModel model;
    private ItemModel cache;

    @Inject
    public ItemDetailsPresenterImp(ItemDetailsModel model) {
        this.model = model;
    }

    @Override
    protected void onViewAttached(@NotNull ItemDetailsActivity view, @Nullable Bundle savedState, @Nullable Bundle data) {

        if (isFirstTimeAttached()) {

            if (savedState == null) {

                if (data == null)
                    throw new NullPointerException("data expected");

                cache = data.getParcelable(ItemDetailsPresenter.ARG_ITEM);

                if(cache == null)
                    throw new IllegalArgumentException("item wasn't passed");

            } else {

                cache = savedState.getParcelable(ARG_CACHE);

                if(cache == null)
                    throw new IllegalArgumentException("item wasn't stored");
            }

            syncWithView();
        }
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        outState.putParcelable(ARG_CACHE, cache);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroyed() {

    }

    private void syncWithView() {

        if(isViewGone())
            throw new IllegalStateException();

        if(cache == null)
            throw new IllegalStateException();

        final ItemDetailsView view = getView();

        final Collection<IUrlFutureBitmap> gallery = cache.getGallery();

        if(!gallery.isEmpty()) {
            view.setGalleryPreviewImage(gallery.iterator().next());
        }
        view.setTitle(cache.getTitle());
        view.setMainInfo(getView().getString(R.string.item_details_main_info,
                cache.getRegion(), cache.getPrice()));
        view.setDescription(cache.getDescription());
        view.setPubDate(cache.getPubDate());
        view.setUsername(cache.getUsername());
        view.setAvatar(cache.getUserAvatar());
    }

    @Override
    public void onShowGallery(@NotNull ImageView clicked) {

        if(cache.getGallery().isEmpty()) return;

        final Intent intent = model.createGalleryIntent(cache.getGallery());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // start smooth transition between activities
            final ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getView(), clicked, clicked.getTransitionName());
            getView().startActivity(intent, options.toBundle());
        } else {
            getView().startActivity(intent);
        }
    }

    @Override
    public void onOpenDialog() {

    }

    @Override
    public void onComplain() {

        final Context context = getView();
        final Intent intent = model.createComplainIntent(context.getString(R.string.support_email),
                context.getString(R.string.item_details_complaint_subj, cache.getUsername()),
                context.getString(R.string.item_details_complaint_body, cache.getId(), cache.getUsername()));

        try {
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.item_details_email_client_chooser)));
        } catch (final ActivityNotFoundException e) {
            getView().showText(context.getString(R.string.item_details_no_email_client));
        }
    }
}
