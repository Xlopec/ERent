package com.ua.erent.module.core.presentation.mvp.presenter.interfaces;

import android.content.Intent;

import com.ua.erent.module.core.presentation.mvp.view.util.IUrlFutureBitmap;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by Максим on 12/3/2016.
 */

public interface ItemDetailsModel {

    Intent createGalleryIntent(@NotNull Collection<IUrlFutureBitmap> gallery);

    Intent createComplainIntent(@NotNull String email, @NotNull String subject, @NotNull String body);

}
