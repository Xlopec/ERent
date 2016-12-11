package com.ua.erent.module.core.presentation.mvp.module;

import android.content.Context;

import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.item.domain.ICategoryAppService;
import com.ua.erent.module.core.item.domain.IItemAppService;
import com.ua.erent.module.core.networking.util.ConnectionManager;
import com.ua.erent.module.core.presentation.mvp.model.ItemCreationModelImp;
import com.ua.erent.module.core.presentation.mvp.model.ItemsModel;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.IItemsModel;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.ItemCreationModel;
import com.ua.erent.module.core.presentation.mvp.presenter.ItemCreationPresenterImp;
import com.ua.erent.module.core.presentation.mvp.presenter.ItemsPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IItemsPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ItemCreationPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 10/11/2016.
 */
@Module
public final class ItemsModule {

    @Provides
    @ActivityScope
    IItemsModel provideItemsModel(Context context, IItemAppService appService, IAuthAppService authAppService) {
        return new ItemsModel(context, appService, authAppService);
    }

    @Provides
    @ActivityScope
    IItemsPresenter provideItemsPresenter(IItemsModel model) {
        return new ItemsPresenter(model);
    }

    @Provides
    @ActivityScope
    ItemCreationModel provideItemCreationModel(Context context, ICategoryAppService categoryAppService,
                                               ConnectionManager connectionManager, IItemAppService iItemAppService) {
        return new ItemCreationModelImp(context, categoryAppService, connectionManager, iItemAppService);
    }

    @Provides
    @ActivityScope
    ItemCreationPresenter provideItemCreationPresenter(ItemCreationModel model) {
        return new ItemCreationPresenterImp(model);
    }

}
