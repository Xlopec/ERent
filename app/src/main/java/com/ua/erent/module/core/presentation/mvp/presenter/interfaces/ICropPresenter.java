package com.ua.erent.module.core.presentation.mvp.presenter.interfaces;

import android.net.Uri;

import com.theartofdev.edmodo.cropper.CropImageView;
import com.ua.erent.module.core.presentation.mvp.core.IBasePresenter;
import com.ua.erent.module.core.presentation.mvp.view.ImageCropActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Максим on 10/29/2016.
 */

public abstract class ICropPresenter extends IBasePresenter<ImageCropActivity> {

    public abstract void onCropFinished(@NotNull CropImageView.CropResult result);

    @Nullable public abstract Uri onSaveCroppedImage();

}
