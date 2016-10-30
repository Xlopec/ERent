package com.ua.erent.module.core.presentation.mvp.module;

import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.presentation.mvp.model.ImageCropModel;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.IImageCropModel;
import com.ua.erent.module.core.presentation.mvp.presenter.CropPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ICropPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 10/29/2016.
 */
@Module
public final class CropModule {

    @Provides
    @ActivityScope
    IImageCropModel provideModel() {
        return new ImageCropModel();
    }

    @Provides
    @ActivityScope
    ICropPresenter provideCropPresenter(IImageCropModel model) {
        return new CropPresenter(model);
    }

}
