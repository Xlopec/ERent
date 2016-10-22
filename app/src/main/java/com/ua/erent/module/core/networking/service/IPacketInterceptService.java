package com.ua.erent.module.core.networking.service;

import org.jetbrains.annotations.NotNull;

import okhttp3.Response;
import rx.Subscription;
import rx.functions.Action1;

/**
 * <p>
 * Service which intercepts api responses and
 * populates it through observers
 * </p>
 * Created by Максим on 10/21/2016.
 */

public interface IPacketInterceptService {
    /**
     * intercepts server response and fires subscribed observers
     *
     * @param response response to populate
     */
    void intercept(@NotNull Response response);

    /**
     * adds observer to be fired on new response event
     *
     * @param subscriber subscriber instance
     * @return instance of {@linkplain Subscription}
     */
    Subscription addResponseObserver(@NotNull Action1<Response> subscriber);

}
