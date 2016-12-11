package com.ua.erent.module.core.presentation.mvp.module;

import android.content.Context;

import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.item.domain.IItemAppService;
import com.ua.erent.module.core.presentation.mvp.model.MyItemsModel;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.IMyItemsModel;
import com.ua.erent.module.core.presentation.mvp.presenter.MyItemsPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IMyItemsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 10/11/2016.
 */
@Module
public final class MyItemsModule {

    @Provides
    @ActivityScope
    IMyItemsModel provideModel(Context context, IItemAppService appService, IAuthAppService authAppService) {
        return new MyItemsModel(context, appService, authAppService);
    }

    @Provides
    @ActivityScope
    IMyItemsPresenter providePresenter(IMyItemsModel model) {
        return new MyItemsPresenter(model);
    }

}
