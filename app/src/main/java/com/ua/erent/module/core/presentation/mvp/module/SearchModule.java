package com.ua.erent.module.core.presentation.mvp.module;

import com.ua.erent.module.core.di.scopes.FragmentScope;
import com.ua.erent.module.core.item.domain.ICategoryAppService;
import com.ua.erent.module.core.item.domain.IItemAppService;
import com.ua.erent.module.core.networking.util.ConnectionManager;
import com.ua.erent.module.core.presentation.mvp.model.SearchModel;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.ISearchModel;
import com.ua.erent.module.core.presentation.mvp.presenter.SearchPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ISearchPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 12/5/2016.
 */
@Module
public final class SearchModule {

    @Provides
    @FragmentScope
    ISearchModel provideModel(ICategoryAppService categoryAppService,
                              ConnectionManager connectionManager, IItemAppService itemAppService) {
        return new SearchModel(categoryAppService, connectionManager, itemAppService);
    }

    @Provides
    @FragmentScope
    ISearchPresenter providePresenter(ISearchModel model) {
        return new SearchPresenter(model);
    }

}
