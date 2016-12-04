package com.ua.erent.module.core.presentation.mvp.module;

import android.content.Context;

import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.presentation.mvp.model.ItemDetailsModelImp;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.IItemsModel;
import com.ua.erent.module.core.presentation.mvp.presenter.ItemDetailsPresenterImp;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ItemDetailsModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ItemDetailsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 12/3/2016.
 */
@Module
public final class ItemDetailsModule {

    @Provides
    @ActivityScope
    ItemDetailsModel provideItemModel(Context context) {
        return new ItemDetailsModelImp(context);
    }

    @Provides
    @ActivityScope
    ItemDetailsPresenter provideItemPresenter(ItemDetailsModel model) {
        return new ItemDetailsPresenterImp(model);
    }

}
