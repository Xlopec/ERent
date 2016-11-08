package com.ua.erent.module.core.item.sync.api;

import com.ua.erent.module.core.item.domain.bo.Item;
import com.ua.erent.module.core.item.domain.vo.ItemID;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import dagger.internal.Preconditions;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
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
        Observable<Collection<ItemResponse>> getItems();

        /**
         * Fetches specified item from the api server
         *
         * @param id item's id to fetch
         * @return observable to monitor process
         */
        @GET("things/{id}")
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
    public Observable<Item> fetchItem(@NotNull ItemID id) {
        return api.getItemForId(id.getId()).observeOn(AndroidSchedulers.mainThread()).map(ConverterFactory::toItem);
    }
}
