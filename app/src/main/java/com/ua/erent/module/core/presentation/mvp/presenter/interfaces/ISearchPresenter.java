package com.ua.erent.module.core.presentation.mvp.presenter.interfaces;

import com.ua.erent.module.core.presentation.mvp.core.IBasePresenter;
import com.ua.erent.module.core.presentation.mvp.view.AdvancedSearchFragment;

import org.jetbrains.annotations.Nullable;

/**
 * Created by Максим on 12/5/2016.
 */

public abstract class ISearchPresenter extends IBasePresenter<AdvancedSearchFragment> {

    public abstract void onCategorySelectionChanged(long id, boolean selected);

    public abstract void onBrandSelectionChanged(long id, boolean selected);

    public abstract void onRegionSelectionChanged(long id, boolean selected);

    public abstract void onSearch(@Nullable String query);

    public abstract void onNextResult();

}
