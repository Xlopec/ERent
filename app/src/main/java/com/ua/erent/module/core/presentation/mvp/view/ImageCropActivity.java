package com.ua.erent.module.core.presentation.mvp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImageView;
import com.ua.erent.R;
import com.ua.erent.module.core.presentation.mvp.component.CropComponent;
import com.ua.erent.module.core.di.target.InjectableActivity;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ICropPresenter;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.ICropView;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import dagger.internal.Preconditions;

public final class ImageCropActivity extends InjectableActivity<ImageCropActivity, ICropPresenter>
        implements ICropView, CropImageView.OnCropImageCompleteListener {

    public static final String ARG_IMG_URI = "argCropImageUri";
    public static final String ARG_CROPPED_IMG_URI = "argCroppedImageUri";
    public static final String ARG_ASPECT_RATIO_X = "argAspectRatioX";
    public static final String ARG_ASPECT_RATIO_Y = "argAspectRatioY";
    public static final String ARG_FIXED_ASPECT_RATIO = "argFixedAspectRatio";
    public static final String ARG_SHAPE = "argType";

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.crop_view)
    protected CropImageView cropImageView;

    @BindView(R.id.fab)
    protected FloatingActionButton fab;

    public enum Shape {
        OVAL, RECT
    }

    public ImageCropActivity() {
        super(R.layout.activity_image_crop, CropComponent.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        cropImageView.setOnCropImageCompleteListener(this);

        fab.setOnClickListener(v -> {

            final Uri uri = presenter.onSaveCroppedImage();

            if(uri != null) {
                cropImageView.saveCroppedImageAsync(uri, Bitmap.CompressFormat.PNG, 100);
            }
        });
    }

    @NotNull
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setAspectRatio(int x, int y) {
        cropImageView.setAspectRatio(x, y);
    }

    @Override
    public void setFixedAspectRatio(boolean fixed) {
        cropImageView.setFixedAspectRatio(fixed);
    }

    @Override
    public void setCropShape(@NotNull CropImageView.CropShape shape) {
        cropImageView.setCropShape(shape);
    }

    @Override
    public void setScaleType(@NotNull CropImageView.ScaleType scaleType) {
        cropImageView.setScaleType(scaleType);
    }

    @Override
    public void setTitle(String title) {
        //final ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle(title);
    }

    @Override
    public void setCropUrl(@NotNull Uri uri) {
        Preconditions.checkNotNull(uri);
        cropImageView.setImageUriAsync(uri);
    }

    @Override
    public void showToast(@NotNull String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        presenter.onCropFinished(result);
    }
}
