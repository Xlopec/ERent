package com.ua.erent.module.core.presentation.mvp.model;

import com.ua.erent.module.core.item.domain.ICategoryAppService;
import com.ua.erent.module.core.item.domain.IItemAppService;
import com.ua.erent.module.core.item.domain.vo.Brand;
import com.ua.erent.module.core.item.domain.vo.Region;
import com.ua.erent.module.core.networking.util.ConnectionManager;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.ISearchModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.BrandModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.RegionModel;
import com.ua.erent.module.core.presentation.mvp.util.CategoriesConverter;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Максим on 12/5/2016.
 */

public final class SearchModel implements ISearchModel {

    private final ICategoryAppService categoryAppService;
    private final ConnectionManager connectionManager;
    private final IItemAppService itemAppService;

    @Inject
    public SearchModel(ICategoryAppService categoryAppService, ConnectionManager connectionManager,
                       IItemAppService itemAppService) {
        this.categoryAppService = categoryAppService;
        this.connectionManager = connectionManager;
        this.itemAppService = itemAppService;
    }

    @Override
    public boolean hasConnection() {
        return connectionManager.hasConnection();
    }

    @Override
    public Collection<CategoryModel> getCategories() {
        return CategoriesConverter.toModel(categoryAppService.getCachedCategories());
    }

    @Override
    public Observable<Collection<CategoryModel>> fetchCategories() {
        return categoryAppService.fetchCategories().map(CategoriesConverter::toModel);
    }

    @Override
    public Observable<Collection<BrandModel>> fetchBrands() {
        return itemAppService.fetchBrands().map(SearchModel::toBrandModel);
    }

    @Override
    public Observable<Collection<RegionModel>> fetchRegions() {
        return itemAppService.fetchRegions().map(SearchModel::toRegionModel);
    }

    private static Collection<BrandModel> toBrandModel(Collection<Brand> from) {
        final Collection<BrandModel> result = new ArrayList<>(from.size());

        for (final Brand brand : from) {
            result.add(new BrandModel(brand.getId(), brand.getName(), brand.getDescription()));
        }
        return result;
    }

    private static Collection<RegionModel> toRegionModel(Collection<Region> from) {
        final Collection<RegionModel> result = new ArrayList<>(from.size());

        for (final Region region : from) {
            result.add(new RegionModel(region.getId(), region.getName()));
        }
        return result;
    }

}
