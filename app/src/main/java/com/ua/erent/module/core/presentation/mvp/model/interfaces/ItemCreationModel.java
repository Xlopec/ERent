package com.ua.erent.module.core.presentation.mvp.model.interfaces;

import com.ua.erent.module.core.presentation.mvp.presenter.ItemCreationForm;
import com.ua.erent.module.core.presentation.mvp.presenter.model.BrandModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.RegionModel;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import rx.Observable;

/**
 * Created by Максим on 12/9/2016.
 */

public interface ItemCreationModel {
    Observable<Boolean> getNetworkObservable();

    boolean hasConnection();

    Collection<CategoryModel> getCategories();

    Observable<Collection<CategoryModel>> fetchCategories();

    Observable<Collection<BrandModel>> fetchBrands();

    Observable<Void> createItem(@NotNull ItemCreationForm form);

    Observable<Collection<RegionModel>> fetchRegions();
}
