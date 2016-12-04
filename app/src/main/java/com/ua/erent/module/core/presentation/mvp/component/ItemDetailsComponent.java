package com.ua.erent.module.core.presentation.mvp.component;

import com.ua.erent.module.core.app.AppComponent;
import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.presentation.mvp.core.IMVPComponent;
import com.ua.erent.module.core.presentation.mvp.module.ItemDetailsModule;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ItemDetailsPresenter;
import com.ua.erent.module.core.presentation.mvp.view.ItemDetailsActivity;

import dagger.Component;

/**
 * <p>
 * Component to provide test injections
 * </p>
 * Created by Максим on 10/11/2016.
 */
@Component(dependencies = AppComponent.class, modules = ItemDetailsModule.class)
@ActivityScope
public interface ItemDetailsComponent extends IMVPComponent<ItemDetailsActivity, ItemDetailsPresenter> {

}
