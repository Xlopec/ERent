package com.ua.erent.module.core.presentation.mvp.presenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.theartofdev.edmodo.cropper.CropImageView;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.IImageCropModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ICropPresenter;
import com.ua.erent.module.core.presentation.mvp.view.ImageCropActivity;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.ICropView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

import javax.inject.Inject;

import static android.R.attr.x;
import static android.R.attr.y;

/**
 * Created by Максим on 10/29/2016.
 */

public final class CropPresenter extends ICropPresenter {

    private static final String ARG_CROP_URI = "argCropUriState";
    private static final String ARG_FIXED_RATIO = "argFixedRatioState";
    private static final String ARG_RATIO_X = "argRatioXState";
    private static final String ARG_RATIO_Y = "argRatioYState";
    private static final String ARG_SHAPE = "argShapeState";

    private final IImageCropModel model;

    private Uri uri;
    private boolean isFixedAspectRatio;
    private int aspectX, aspectY;
    private CropImageView.CropShape shape;

    @Inject
    public CropPresenter(IImageCropModel model) {
        this.model = model;
    }

    @Override
    protected void onViewAttached(@NotNull ImageCropActivity view, @Nullable Bundle savedState, @Nullable Bundle data) {

        if (isFirstTimeAttached()) {

            if (savedState == null) {

                if (data == null)
                    throw new IllegalArgumentException("data weren't passed");

                processBundle(data);
            } else {
                restoreState(savedState);
            }
        }
        syncWithView();
    }

    @Override
    protected void onDestroyed() {

    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_CROP_URI, uri);
        outState.putBoolean(ARG_FIXED_RATIO, isFixedAspectRatio);
        outState.putInt(ARG_RATIO_X, aspectX);
        outState.putInt(ARG_RATIO_Y, aspectY);
        outState.putSerializable(ARG_SHAPE, shape);
    }

    private void restoreState(Bundle state) {
        uri = state.getParcelable(ARG_CROP_URI);
        isFixedAspectRatio = state.getBoolean(ARG_FIXED_RATIO);
        aspectX = state.getInt(ARG_RATIO_X);
        aspectY = state.getInt(ARG_RATIO_Y);
        shape = (CropImageView.CropShape) state.getSerializable(ARG_SHAPE);
    }

    @Override
    public void onCropFinished(@NotNull CropImageView.CropResult result) {

        if (result.isSuccessful()) {

            final Intent intent = new Intent();
            // pass result back
            intent.putExtra(ImageCropActivity.ARG_CROPPED_IMG_URI, result.getUri());
            getView().setResult(Activity.RESULT_OK, intent);
            getView().finish();

        } else {
            getView().showToast(result.getError().getMessage());
        }
    }

    @NotNull
    @Override
    public String getImageFormat() {
        return model.getImageFormat();
    }

    @Nullable
    @Override
    public Uri onSaveCroppedImage() {

        final Uri uri = model.createStoreFileUri(getView());

        if(uri == null) {
            getView().showToast("Failed to create temp file to store image");
        }

        return uri;
    }

    private void syncWithView() {

        final ICropView view = getView();

        if (uri == null)
            throw new IllegalStateException("uri == null");

        final String path = uri.getPath();

        view.setTitle(path.substring(path.lastIndexOf('/') + 1));
        view.setCropUrl(uri);
        view.setScaleType(CropImageView.ScaleType.CENTER_CROP);

        if (isFixedAspectRatio) {
            view.setFixedAspectRatio(true);
            view.setAspectRatio(x, y);
        } else {
            view.setFixedAspectRatio(false);
        }

        if (shape != null) {
            view.setCropShape(shape);
        }
    }

    private void processBundle(Bundle bundle) {

        final ICropView view = getView();

        view.setScaleType(CropImageView.ScaleType.CENTER_CROP);
        uri = bundle.getParcelable(ImageCropActivity.ARG_IMG_URI);
        isFixedAspectRatio = bundle.getBoolean(ImageCropActivity.ARG_FIXED_ASPECT_RATIO);

        if (isFixedAspectRatio) {

            aspectX = bundle.getInt(ImageCropActivity.ARG_ASPECT_RATIO_X);
            aspectY = bundle.getInt(ImageCropActivity.ARG_ASPECT_RATIO_Y);

            if (aspectX < 0 || aspectY < 0)
                throw new IllegalArgumentException(
                        String.format(Locale.getDefault(), "x or y smaller than zero x=%d,y=%d", x, y));

            view.setFixedAspectRatio(true);
            view.setAspectRatio(aspectX, aspectY);
        } else {
            view.setFixedAspectRatio(false);
        }

        final ImageCropActivity.Shape shape = (ImageCropActivity.Shape)
                bundle.getSerializable(ImageCropActivity.ARG_SHAPE);

        this.shape = CropImageView.CropShape.RECTANGLE;

        if (shape != null && shape == ImageCropActivity.Shape.OVAL) {
            this.shape = CropImageView.CropShape.OVAL;
        }
    }

}
