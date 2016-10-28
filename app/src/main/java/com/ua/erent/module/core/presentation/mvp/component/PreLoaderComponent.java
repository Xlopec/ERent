package com.ua.erent.module.core.presentation.mvp.component;

import com.ua.erent.module.core.account.auth.di.AuthComponent;
import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.presentation.mvp.core.IMVPComponent;
import com.ua.erent.module.core.presentation.mvp.module.PreLoaderModule;
import com.ua.erent.module.core.presentation.mvp.presenter.IPreLoadPresenter;
import com.ua.erent.module.core.presentation.mvp.view.PreLoaderActivity;

import dagger.Component;

/**
 * Created by Максим on 10/27/2016.
 */
@Component(dependencies = AuthComponent.class, modules = PreLoaderModule.class)
@ActivityScope
public interface PreLoaderComponent extends IMVPComponent<PreLoaderActivity, IPreLoadPresenter> {
}
