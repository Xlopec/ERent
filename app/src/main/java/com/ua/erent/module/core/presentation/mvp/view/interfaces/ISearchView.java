package com.ua.erent.module.core.presentation.mvp.view.interfaces;

import com.ua.erent.module.core.presentation.mvp.core.IBaseView;
import com.ua.erent.module.core.presentation.mvp.presenter.model.BrandModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.RegionModel;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by Максим on 12/5/2016.
 */

public interface ISearchView extends IBaseView {

    void setCategories(@NotNull Collection<CategoryModel> categories);

    void setBrands(@NotNull Collection<BrandModel> brands);

    void setRegions(@NotNull Collection<RegionModel> regions);

}
