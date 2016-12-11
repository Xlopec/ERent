package com.ua.erent.module.core.item.domain.api;

import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.item.domain.bo.Item;
import com.ua.erent.module.core.item.domain.vo.ItemForm;
import com.ua.erent.module.core.item.domain.vo.ItemID;
import com.ua.erent.module.core.networking.util.IApiFilter;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import dagger.internal.Preconditions;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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
        Observable<ItemResponse> getItems(@QueryMap Map<String, String> query);

        /**
         * Fetches all items from the api server
         *
         * @return observable to monitor process
         */
        @GET("things")
        @Headers("Content-Type: application/json")
        Observable<ItemResponse> getItems();

        /**
         * Fetches specified item from the api server
         *
         * @param id item's id to fetch
         * @return observable to monitor process
         */
        @GET("things/{id}")
        @Headers("Content-Type: application/json")
        Observable<ItemResponse> getItemForId(@Path("id") long id);

        @PUT("things")
        @Headers("Content-Type: application/json")
        Observable<RespItem> createItem(
                @NotNull @Header("Authorization") String token,
                @NotNull @Body ItemCreationReq form
        );

        @Multipart
        @POST("things/{thingId}/photos")
        Observable<Void> uploadPhoto(
                @Header("Authorization") String token,
                @Path("thingId") long thingId,
                @Part("photos[]\"; filename=\"photo") RequestBody avatar
        );

    }

    private final ItemApi api;

    public ItemProviderImp(@NotNull Retrofit retrofit) {
        this.api = Preconditions.checkNotNull(retrofit).create(ItemApi.class);
    }

    @Override
    public Observable<Collection<Item>> fetchItems() {
        return api.getItems().observeOn(AndroidSchedulers.mainThread())
                .map(itemResp -> ConverterFactory.toItem(itemResp.items));
    }

    @Override
    public Observable<Collection<Item>> fetchItems(@NotNull IApiFilter filter) {
        return api.getItems(filter.toFilter()).observeOn(AndroidSchedulers.mainThread())
                .map(itemResp -> ConverterFactory.toItem(itemResp.items));
    }

    @Override
    public Observable<Item> fetchItem(@NotNull ItemID id) {
        return api.getItemForId(id.getId()).observeOn(AndroidSchedulers.mainThread())
                .map(itemResp -> itemResp.items.isEmpty() ? null :
                        ConverterFactory.toItem(itemResp.items.iterator().next()));
    }

    @Override
    public Observable<Item> createItem(@NotNull Session session, @NotNull ItemForm form) {

        final ItemCreationReq body = new ItemCreationReq();

        body.name = form.getName();
        body.description = form.getDescription();
        body.price = form.getPrice().doubleValue();
        body.brandId = form.getBrandId();
        body.regionId = form.getRegionId();
        body.categoryIds = toPrimitive(form.getCategoryIds().toArray(new Long[form.getCategoryIds().size()]));
        body.visibility = true;

        return api.createItem(session.getToken(), body)
                .map(ConverterFactory::toItem)
                .flatMap(item -> {

                    if (form.getUris().isEmpty()) return Observable.just(item);

                    final Collection<Observable<Void>> photoObs = new ArrayList<>(form.getUris().size());

                    for (final Uri uri : form.getUris()) {
                        final String mimeType = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
                        final RequestBody photoReqBody = RequestBody
                                .create(MediaType.parse("image/".concat(mimeType)), new File(uri.getPath()));

                        photoObs.add(api.uploadPhoto(session.getToken(), item.getId().getId(), photoReqBody));
                    }

                    return Observable.zip(photoObs, args -> item);
                }).observeOn(AndroidSchedulers.mainThread());
    }

    private static long[] toPrimitive(Long[] from) {
        final long[] res = new long[from.length];

        for (int i = 0; i < from.length; ++i) {
            res[i] = from[i];
        }
        return res;
    }

}
