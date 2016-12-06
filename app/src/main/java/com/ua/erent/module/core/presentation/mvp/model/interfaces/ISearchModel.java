package com.ua.erent.module.core.presentation.mvp.model.interfaces;

import com.ua.erent.module.core.presentation.mvp.presenter.model.BrandModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.RegionModel;

import java.util.Collection;

import rx.Observable;

/**
 * Created by Максим on 12/5/2016.
 */

public interface ISearchModel {

    boolean hasConnection();

    Collection<CategoryModel> getCategories();

    Observable<Collection<CategoryModel>> fetchCategories();

    Observable<Collection<BrandModel>> fetchBrands();

    Observable<Collection<RegionModel>> fetchRegions();

    //Observable<Collection<ItemModel>> search();

}
