package com.ua.erent.module.core.presentation.mvp.module;

import android.app.Application;

import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.presentation.mvp.model.ILoginModel;
import com.ua.erent.module.core.presentation.mvp.model.LoginModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ILoginPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.LoginPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 10/15/2016.
 */
@Module
public final class LoginModule {

    private final Application application;

    public LoginModule(Application application) {
        this.application = application;
    }

    @Provides
    @ActivityScope
    ILoginModel provideLoginModel(IAuthAppService handler) {
        return new LoginModel(handler);
    }

    @Provides
    @ActivityScope
    ILoginPresenter provideLoginPresenter(ILoginModel model) {
        return new LoginPresenter(application, model);
    }

}
