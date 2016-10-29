package com.ua.erent.module.core.presentation.mvp.component;

import com.ua.erent.module.core.account.auth.di.AuthComponent;
import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.presentation.mvp.core.IMVPComponent;
import com.ua.erent.module.core.presentation.mvp.module.InitialScreenModule;
import com.ua.erent.module.core.presentation.mvp.presenter.IInitialScreenPresenter;
import com.ua.erent.module.core.presentation.mvp.view.InitialScreenActivity;

import dagger.Component;

/**
 * Created by Максим on 10/28/2016.
 */
@Component(dependencies = AuthComponent.class, modules = InitialScreenModule.class)
@ActivityScope
public interface InitialScreenComponent extends IMVPComponent<InitialScreenActivity, IInitialScreenPresenter> {
}
