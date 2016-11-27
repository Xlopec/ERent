package com.ua.erent.module.core.presentation.mvp.model;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.ua.erent.module.core.presentation.mvp.model.interfaces.IImageCropModel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * Created by Максим on 10/30/2016.
 */

public final class ImageCropModel implements IImageCropModel {

    private static final String IMAGE_FORMAT = "png";

    private static final String TAG = ImageCropModel.class.getSimpleName();

    @Override
    public String getImageFormat() {
        return IMAGE_FORMAT;
    }

    @Override
    public Uri createStoreFileUri(@NotNull Context context) {

        try {
            return Uri.fromFile(File.createTempFile("image", ".".concat(IMAGE_FORMAT), context.getExternalCacheDir()));
        } catch (final IOException e) {
            Log.e(TAG, "Exception occurred while creating temp file", e);
        }

        return null;
    }
}
