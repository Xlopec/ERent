package com.ua.erent.module.core.item.domain.api;

import com.ua.erent.module.core.item.domain.bo.Category;

import java.util.Collection;

import rx.Observable;

/**
 * Created by Максим on 11/13/2016.
 */

public interface ICategoriesProvider {

    Observable<Collection<Category>> fetchCategories();

    Observable<Category> fetchCategory(long id);

}
