package com.ua.erent.module.core.presentation.mvp.module;

import android.content.Context;

import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.item.domain.IItemAppService;
import com.ua.erent.module.core.presentation.mvp.model.ItemsModel;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.IItemsModel;
import com.ua.erent.module.core.presentation.mvp.presenter.ItemsPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IItemsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 10/11/2016.
 */
@Module
public final class ItemsModule {

    @Provides
    @ActivityScope
    IItemsModel provideItemsModel(Context context, IItemAppService appService) {
        return new ItemsModel(context, appService);
    }

    @Provides
    @ActivityScope
    IItemsPresenter provideItemsPresenter(IItemsModel model) {
        return new ItemsPresenter(model);
    }

}
