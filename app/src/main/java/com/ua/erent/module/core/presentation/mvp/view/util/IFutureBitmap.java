package com.ua.erent.module.core.presentation.mvp.view.util;

import android.graphics.Bitmap;
import android.support.annotation.Px;

import org.jetbrains.annotations.NotNull;

import rx.Observable;

/**
 * Created by Максим on 11/12/2016.
 */

public interface IFutureBitmap {

    @NotNull
    Observable<Bitmap> fetch(@Px int width, @Px int height);

}
