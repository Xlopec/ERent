package com.ua.erent.module.core.presentation.mvp.component;


import com.ua.erent.module.core.app.AppComponent;
import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.presentation.mvp.core.IMVPComponent;
import com.ua.erent.module.core.presentation.mvp.module.CategoriesModule;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ICategoriesPresenter;
import com.ua.erent.module.core.presentation.mvp.view.CategoriesActivity;

import dagger.Component;

/**
 * Created by Максим on 11/12/2016.
 */
@Component(dependencies = AppComponent.class, modules = CategoriesModule.class)
@ActivityScope
public interface CategoriesComponent extends IMVPComponent<CategoriesActivity, ICategoriesPresenter> {
}
