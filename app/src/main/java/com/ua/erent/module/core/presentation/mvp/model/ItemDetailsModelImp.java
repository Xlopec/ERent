package com.ua.erent.module.core.presentation.mvp.model;

import android.content.Context;
import android.content.Intent;

import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ItemDetailsModel;
import com.ua.erent.module.core.presentation.mvp.view.GalleryActivity;
import com.ua.erent.module.core.presentation.mvp.view.util.IUrlFutureBitmap;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Максим on 12/3/2016.
 */

public final class ItemDetailsModelImp implements ItemDetailsModel {

    private final Context context;

    public ItemDetailsModelImp(Context context) {
        this.context = context;
    }

    @Override
    public Intent createGalleryIntent(@NotNull Collection<IUrlFutureBitmap> gallery) {
        final Intent intent = new Intent(context, GalleryActivity.class);
        intent.putParcelableArrayListExtra(GalleryActivity.ARG_IMAGES, new ArrayList<>(gallery));
        return intent;
    }

    @Override
    public Intent createComplainIntent(@NotNull String email, @NotNull String subject, @NotNull String body) {
        final Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        return intent;
    }
}
