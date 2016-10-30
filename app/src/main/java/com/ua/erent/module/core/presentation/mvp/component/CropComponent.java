package com.ua.erent.module.core.presentation.mvp.component;

import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.presentation.mvp.core.IMVPComponent;
import com.ua.erent.module.core.presentation.mvp.module.CropModule;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ICropPresenter;
import com.ua.erent.module.core.presentation.mvp.view.ImageCropActivity;

import dagger.Component;

/**
 * Created by Максим on 10/29/2016.
 */
@Component(modules = CropModule.class)
@ActivityScope
public interface CropComponent extends IMVPComponent<ImageCropActivity, ICropPresenter> {
}
