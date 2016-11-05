package com.ua.erent.module.core.presentation.mvp.component;

import com.ua.erent.module.core.app.AppComponent;
import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.presentation.mvp.core.IMVPComponent;
import com.ua.erent.module.core.presentation.mvp.module.InitialScreenModule;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IInitialScreenPresenter;
import com.ua.erent.module.core.presentation.mvp.view.InitialScreenActivity;

import dagger.Component;

/**
 * Created by Максим on 10/28/2016.
 */
@Component(dependencies = AppComponent.class, modules = InitialScreenModule.class)
@ActivityScope
public interface InitialScreenComponent extends IMVPComponent<InitialScreenActivity, IInitialScreenPresenter> {
}
