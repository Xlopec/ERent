package com.ua.erent.module.core.presentation.mvp.component;

import com.ua.erent.module.core.app.AppComponent;
import com.ua.erent.module.core.di.scopes.FragmentScope;
import com.ua.erent.module.core.presentation.mvp.core.IMVPComponent;
import com.ua.erent.module.core.presentation.mvp.module.RegisterModule;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IRegisterPresenter;
import com.ua.erent.module.core.presentation.mvp.view.RegisterFragment;

import dagger.Component;

/**
 * Created by Максим on 10/27/2016.
 */
@Component(dependencies = AppComponent.class, modules = RegisterModule.class)
@FragmentScope
public interface RegisterComponent extends IMVPComponent<RegisterFragment, IRegisterPresenter> {
}
