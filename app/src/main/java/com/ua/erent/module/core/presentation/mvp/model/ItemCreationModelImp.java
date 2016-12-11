package com.ua.erent.module.core.presentation.mvp.model;

import android.content.Context;

import com.ua.erent.module.core.item.domain.ICategoryAppService;
import com.ua.erent.module.core.item.domain.IItemAppService;
import com.ua.erent.module.core.item.domain.vo.ItemForm;
import com.ua.erent.module.core.networking.util.ConnectionManager;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.ItemCreationModel;
import com.ua.erent.module.core.presentation.mvp.presenter.ItemCreationForm;
import com.ua.erent.module.core.presentation.mvp.presenter.model.BrandModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.RegionModel;
import com.ua.erent.module.core.presentation.mvp.util.ItemConverter;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Максим on 12/9/2016.
 */

public final class ItemCreationModelImp implements ItemCreationModel {

    private final Context context;
    private final ICategoryAppService categoryAppService;
    private final ConnectionManager connectionManager;
    private final IItemAppService itemAppService;

    @Inject
    public ItemCreationModelImp(Context context, ICategoryAppService categoryAppService, ConnectionManager connectionManager,
                                IItemAppService itemAppService) {
        this.context = context;
        this.categoryAppService = categoryAppService;
        this.connectionManager = connectionManager;
        this.itemAppService = itemAppService;
    }

    @Override
    public Observable<Boolean> getNetworkObservable() {
        return connectionManager.getNetworkObservable();
    }

    @Override
    public boolean hasConnection() {
        return connectionManager.hasConnection();
    }

    @Override
    public Collection<CategoryModel> getCategories() {
        return ItemConverter.toCategoryModel(categoryAppService.getCachedCategories());
    }

    @Override
    public Observable<Collection<CategoryModel>> fetchCategories() {
        return categoryAppService.fetchCategories().map(ItemConverter::toCategoryModel);
    }

    @Override
    public Observable<Collection<BrandModel>> fetchBrands() {
        return itemAppService.fetchBrands().map(ItemConverter::toBrandModel);
    }

    @Override
    public Observable<Void> createItem(@NotNull ItemCreationForm form) {
        return itemAppService.createItem(new ItemForm.Builder(form.getName(), form.getDescription(),
                new BigDecimal(form.getPrice()), form.getBrandId(), form.getRegionId())
                .addCategory(form.getCategoryId())
                .addImage(form.getUris())
                .build())
                .map(item -> null);
    }

    @Override
    public Observable<Collection<RegionModel>> fetchRegions() {
        return itemAppService.fetchRegions().map(ItemConverter::toRegionModel);
    }

}
