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

    /**
     * Runs component initialization. Classes which implement
     * this interface, should provide their own behaviour
     *
     * @param session session to provide access to server api (if needed)
     */
    Observable<Initializeable> initialize(@NotNull Session session);

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

    /**
     * Checks whether this instance is already initialized or not
     *
     * @return true if instance is already initialized or false in another case
     */
    boolean isInitialized();

}
