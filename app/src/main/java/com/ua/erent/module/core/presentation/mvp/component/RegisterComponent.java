package com.ua.erent.module.core.presentation.mvp.component;

import com.ua.erent.module.core.account.auth.di.AuthComponent;
import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.presentation.mvp.core.IMVPComponent;
import com.ua.erent.module.core.presentation.mvp.module.RegisterModule;
import com.ua.erent.module.core.presentation.mvp.presenter.IRegisterPresenter;
import com.ua.erent.module.core.presentation.mvp.view.RegisterFragment;

import dagger.Component;

/**
 * Created by Максим on 10/27/2016.
 */
@Component(dependencies = AuthComponent.class, modules = RegisterModule.class)
@ActivityScope
public interface RegisterComponent extends IMVPComponent<RegisterFragment, IRegisterPresenter> {
}
