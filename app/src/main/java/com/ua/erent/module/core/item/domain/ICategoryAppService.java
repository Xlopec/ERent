package com.ua.erent.module.core.item.domain;

import com.ua.erent.module.core.item.domain.bo.Category;

import java.util.Collection;

import rx.Observable;

/**
 * Created by Максим on 11/12/2016.
 */

public interface ICategoryAppService {

    Collection<Category> getCachedCategories();

    Observable<Collection<Category>> fetchCategories();

    Observable<Collection<Category>> getOnCategoriesAddedObs();

    Observable<Collection<Category>> getOnCategoriesDeletedObs();

}
