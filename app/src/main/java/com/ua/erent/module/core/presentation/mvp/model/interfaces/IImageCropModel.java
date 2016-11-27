package com.ua.erent.module.core.presentation.mvp.model.interfaces;

import android.content.Context;
import android.net.Uri;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 10/30/2016.
 */

public interface IImageCropModel {

    String getImageFormat();

    Uri createStoreFileUri(@NotNull Context context);

}
