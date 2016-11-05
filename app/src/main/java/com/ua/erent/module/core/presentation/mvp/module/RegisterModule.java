package com.ua.erent.module.core.presentation.mvp.module;

import android.app.Application;

import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.di.scopes.FragmentScope;
import com.ua.erent.module.core.presentation.mvp.model.RegisterModel;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.IRegisterModel;
import com.ua.erent.module.core.presentation.mvp.presenter.RegisterPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IRegisterPresenter;

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
    @FragmentScope
    IRegisterModel provideRegisterModel(IAuthAppService authAppService) {
        return new RegisterModel(application, authAppService);
    }

    @Provides
    @FragmentScope
    IRegisterPresenter provideRegisterPresenter(IRegisterModel model) {
        return new RegisterPresenter(model);
    }

}
