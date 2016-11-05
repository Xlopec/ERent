package com.ua.erent.module.core.init.domain;

import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.init.IInitCallback;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by Максим on 11/5/2016.
 */

public interface IInitAppService {

    void registerInitializeable(@NotNull Collection<Initializeable> initializeables);

    void registerInitializeable(@NotNull Initializeable initializeable);

    void unregisterInitializeable(@NotNull Initializeable initializeable);

    Collection<Initializeable> getRegisteredInitializeables();

    void initialize(@NotNull Session session, @NotNull IInitCallback callback);

}
