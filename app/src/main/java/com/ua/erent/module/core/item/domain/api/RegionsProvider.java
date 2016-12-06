package com.ua.erent.module.core.item.domain.api;

import com.ua.erent.module.core.item.domain.vo.Region;

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

public final class RegionsProvider implements IRegionsProvider {

    private interface RegionsApi {

        @GET("regions")
        @Headers("Content-Type: application/json")
        Observable<Collection<RegionsResponse>> fetchRegions();

    }

    private final RegionsApi api;

    public RegionsProvider(@NotNull Retrofit retrofit) {
        this.api = Preconditions.checkNotNull(retrofit).create(RegionsApi.class);
    }

    @Override
    public Observable<Collection<Region>> fetchRegions() {
        return api.fetchRegions().observeOn(AndroidSchedulers.mainThread()).map(RegionsProvider::toRegions);
    }

    private static Collection<Region> toRegions(Collection<RegionsResponse> src) {
        final Collection<Region> result = new ArrayList<>(src.size());

        for (final RegionsResponse resp : src) {
            result.add(new Region(resp.id, resp.name));
        }
        return result;
    }

}
