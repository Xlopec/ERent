package com.ua.erent.module.core.item.sync.api;

import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.item.domain.bo.Item;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by Максим on 11/7/2016.
 */

public final class ItemProviderImp implements ItemProvider {
    public ItemProviderImp(Retrofit retrofit) {

    }

    @Override
    public Observable<Collection<Item>> fetchItems(@NotNull Session session) {
        return null;
    }
}
