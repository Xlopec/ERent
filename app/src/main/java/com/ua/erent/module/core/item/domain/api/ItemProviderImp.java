package com.ua.erent.module.core.item.domain.api;

import com.ua.erent.module.core.item.domain.bo.Item;
import com.ua.erent.module.core.item.domain.vo.ItemID;
import com.ua.erent.module.core.networking.util.IApiFilter;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

import dagger.internal.Preconditions;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Максим on 11/7/2016.
 */

public final class ItemProviderImp implements ItemProvider {

    /**
     * Represents server api for items
     */
    private interface ItemApi {

        /**
         * Fetches all items from the api server
         *
         * @return observable to monitor process
         */
        @GET("things")
        @Headers("Content-Type: application/json")
        Observable<Collection<ItemResponse>> getItems(@QueryMap Map<String, String> query);

        /**
         * Fetches all items from the api server
         *
         * @return observable to monitor process
         */
        @GET("things")
        @Headers("Content-Type: application/json")
        Observable<Collection<ItemResponse>> getItems();

        /**
         * Fetches specified item from the api server
         *
         * @param id item's id to fetch
         * @return observable to monitor process
         */
        @GET("things/{id}")
        @Headers("Content-Type: application/json")
        Observable<ItemResponse> getItemForId(@Path("id") long id);

    }

    private final ItemApi api;

    public ItemProviderImp(@NotNull Retrofit retrofit) {
        this.api = Preconditions.checkNotNull(retrofit).create(ItemApi.class);
    }

    @Override
    public Observable<Collection<Item>> fetchItems() {
        return api.getItems().observeOn(AndroidSchedulers.mainThread()).map(ConverterFactory::toItem);
    }

    @Override
    public Observable<Collection<Item>> fetchItems(@NotNull IApiFilter filter) {
        return api.getItems().observeOn(AndroidSchedulers.mainThread()).map(ConverterFactory::toItem);
    }

    @Override
    public Observable<Item> fetchItem(@NotNull ItemID id) {
        return api.getItemForId(id.getId()).observeOn(AndroidSchedulers.mainThread()).map(ConverterFactory::toItem);
    }
}
