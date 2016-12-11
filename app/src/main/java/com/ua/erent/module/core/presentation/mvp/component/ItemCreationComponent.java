package com.ua.erent.module.core.presentation.mvp.component;

import com.ua.erent.module.core.app.AppComponent;
import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.presentation.mvp.core.IMVPComponent;
import com.ua.erent.module.core.presentation.mvp.module.ItemsModule;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ItemCreationPresenter;
import com.ua.erent.module.core.presentation.mvp.view.ItemCreateActivity;

import dagger.Component;

/**
 * <p>
 * Component to provide test injections
 * </p>
 * Created by Максим on 10/11/2016.
 */
@Component(dependencies = AppComponent.class, modules = ItemsModule.class)
@ActivityScope
public interface ItemCreationComponent extends IMVPComponent<ItemCreateActivity, ItemCreationPresenter> {

}
