package com.ua.erent.module.core.item.domain.api;

import com.ua.erent.module.core.item.domain.bo.Category;
import com.ua.erent.module.core.item.domain.vo.CategoryID;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Максим on 11/13/2016.
 */

public final class CategoriesProvider implements ICategoriesProvider {

    private interface CategoriesAPI {

        /**
         * Fetches categories asynchronously from API server
         *
         * @return rx observable to monitor process state
         */
        @GET("categories")
        Observable<Collection<CategoriesResponse>> fetchCategories();

        /**
         * Fetches category asynchronously from API server by its id
         *
         * @param categoryId category id to fetch
         * @return rx observable to monitor process state
         */
        @GET("categories/{id}")
        Observable<CategoriesResponse> fetchCategory(@Path("id") long categoryId);

    }

    private final CategoriesAPI api;

    @Inject
    public CategoriesProvider(Retrofit retrofit) {
        this.api = retrofit.create(CategoriesAPI.class);
    }

    @Override
    public Observable<Collection<Category>> fetchCategories() {
        return api.fetchCategories().map(CategoriesProvider::toBo);
    }

    @Override
    public Observable<Category> fetchCategory(long id) {
        return api.fetchCategory(id).map(CategoriesProvider::toBo);
    }

    private static Collection<Category> toBo(Collection<CategoriesResponse> src) {

        final Collection<Category> result = new ArrayList<>(src.size());

        for (final CategoriesResponse response : src) {
            result.add(CategoriesProvider.toBo(response));
        }
        return result;
    }

    private static Category toBo(CategoriesResponse response) {
        return new Category(response.name, new CategoryID(response.id), response.description);
    }

}
