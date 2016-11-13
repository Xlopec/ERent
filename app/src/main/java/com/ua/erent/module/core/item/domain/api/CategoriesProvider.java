package com.ua.erent.module.core.item.domain.api;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * Created by Максим on 11/13/2016.
 */

public final class CategoriesProvider implements ICategoriesProvider {

    private interface CategoriesAPI {

    }

    private final CategoriesAPI api;

    @Inject
    public CategoriesProvider(Retrofit retrofit) {
        this.api = retrofit.create(CategoriesAPI.class);
    }
}
