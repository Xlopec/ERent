package com.ua.erent.module.core.presentation.mvp.component;

import com.ua.erent.module.core.account.auth.di.AuthComponent;
import com.ua.erent.module.core.di.scopes.FragmentScope;
import com.ua.erent.module.core.presentation.mvp.core.IMVPComponent;
import com.ua.erent.module.core.presentation.mvp.module.LoginModule;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ILoginPresenter;
import com.ua.erent.module.core.presentation.mvp.view.LoginFragment;

import dagger.Component;

/**
 * <p>
 * Provides dependencies for signIn operation
 * </p>
 * Created by Максим on 10/15/2016.
 */
@Component(dependencies = AuthComponent.class, modules = LoginModule.class)
@FragmentScope
public interface LoginComponent extends IMVPComponent<LoginFragment, ILoginPresenter> {}
