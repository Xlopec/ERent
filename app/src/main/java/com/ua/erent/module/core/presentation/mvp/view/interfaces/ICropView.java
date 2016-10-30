package com.ua.erent.module.core.presentation.mvp.view.interfaces;

import android.net.Uri;

import com.theartofdev.edmodo.cropper.CropImageView;
import com.ua.erent.module.core.presentation.mvp.core.IBaseView;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 10/29/2016.
 */

public interface ICropView extends IBaseView {

    void setAspectRatio(int x, int y);

    void setFixedAspectRatio(boolean fixed);

    void setCropShape(@NotNull CropImageView.CropShape shape);

    void setScaleType(@NotNull CropImageView.ScaleType scaleType);

    void setTitle(String title);

    void setCropUrl(@NotNull Uri uri);

    void showToast(@NotNull String text);

}
