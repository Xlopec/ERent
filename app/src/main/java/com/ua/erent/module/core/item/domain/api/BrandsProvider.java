package com.ua.erent.module.core.item.domain.api;

import com.ua.erent.module.core.item.domain.vo.Brand;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import dagger.internal.Preconditions;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Максим on 12/6/2016.
 */

public final class BrandsProvider implements IBrandsProvider {

    private interface BrandsApi {

        @GET("brands")
        @Headers("Content-Type: application/json")
        Observable<Collection<BrandsResponse>> fetchBrands();

    }

    private final BrandsApi api;

    public BrandsProvider(@NotNull Retrofit retrofit) {
        this.api = Preconditions.checkNotNull(retrofit).create(BrandsApi.class);
    }

    @Override
    public Observable<Collection<Brand>> fetchBrands() {
        return api.fetchBrands().observeOn(AndroidSchedulers.mainThread()).map(BrandsProvider::toBrands);
    }

    private static Collection<Brand> toBrands(Collection<BrandsResponse> src) {
        final Collection<Brand> result = new ArrayList<>(src.size());

        for (final BrandsResponse resp : src) {
            result.add(new Brand(resp.id, resp.name, resp.descriptiom));
        }
        return result;
    }

}
