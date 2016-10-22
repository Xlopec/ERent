package com.ua.erent.module.core.presentation.mvp.component;

import com.ua.erent.module.core.account.auth.di.AuthComponent;
import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.presentation.mvp.core.IMVPComponent;
import com.ua.erent.module.core.presentation.mvp.module.LoginModule;
import com.ua.erent.module.core.presentation.mvp.presenter.ILoginPresenter;
import com.ua.erent.module.core.presentation.mvp.view.LoginActivity;

import dagger.Component;

/**
 * <p>
 * Provides dependencies for login operation
 * </p>
 * Created by Максим on 10/15/2016.
 */
@Component(dependencies = AuthComponent.class, modules = LoginModule.class)
@ActivityScope
public interface LoginComponent extends IMVPComponent<LoginActivity, ILoginPresenter> {}
