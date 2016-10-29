package com.ua.erent.module.core.presentation.mvp.module;

import android.app.Application;

import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.presentation.mvp.model.IRegisterModel;
import com.ua.erent.module.core.presentation.mvp.model.RegisterModel;
import com.ua.erent.module.core.presentation.mvp.presenter.IRegisterPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.RegisterPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 10/27/2016.
 */
@Module
public final class RegisterModule {

    private final Application application;

    public RegisterModule(Application application) {
        this.application = application;
    }

    @Provides
    @ActivityScope
    IRegisterModel provideRegisterModel(IAuthAppService authAppService) {
        return new RegisterModel(application, authAppService);
    }

    @Provides
    @ActivityScope
    IRegisterPresenter provideRegisterPresenter(IRegisterModel model) {
        return new RegisterPresenter(model);
    }

}
