package com.ua.erent.module.core.presentation.mvp.component;

import com.ua.erent.module.core.app.AppComponent;
import com.ua.erent.module.core.di.scopes.FragmentScope;
import com.ua.erent.module.core.presentation.mvp.core.IMVPComponent;
import com.ua.erent.module.core.presentation.mvp.module.SearchModule;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ISearchPresenter;
import com.ua.erent.module.core.presentation.mvp.view.AdvancedSearchFragment;

import dagger.Component;

/**
 * Created by Максим on 12/5/2016.
 */
@Component(dependencies = AppComponent.class, modules = SearchModule.class)
@FragmentScope
public interface SearchComponent extends IMVPComponent<AdvancedSearchFragment, ISearchPresenter> {
}
