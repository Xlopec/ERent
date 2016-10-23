package com.ua.erent.module.core.util;

import com.ua.erent.module.core.account.auth.bo.Session;

import org.jetbrains.annotations.NotNull;

import rx.Observable;

/**
 * <p>
 * Represents component which can be initialized
 * </p>
 * Created by Максим on 10/14/2016.
 */

public interface Initializeable {

    interface ICallback {

        void onInitialized();

        void onException(@NotNull Throwable th);

    }

    /**
     * Runs component initialization. Classes which implement
     * this interface, should provide their own behaviour
     *
     * @deprecated will be replaced by rx java observable
     * @param session  session to provide access to server api (if needed)
     * @param callback callback to handle initialization process
     */
    @Deprecated
    Observable<Session> initialize(@NotNull Session session, @NotNull ICallback callback);

    /**
     * Handles reject event which may occur while initializing
     */
    void onReject();

    /**
     * Defines whether in case of initialization error (consumed by corresponding callback)
     * global initialization process should be stopped
     *
     * @return flag which defines exception handling strategy
     */
    boolean failOnException();

}
