package com.ua.erent.module.core.util;

import com.ua.erent.module.core.account.auth.bo.Session;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 10/14/2016.
 */

public interface Initializeable {

    interface ICallback {

        void onInitialized();

        void onException(@NotNull Throwable th);

    }

    void initialize(@NotNull Session session, @NotNull ICallback callback);

    void reject();

    boolean failOnException();

}
