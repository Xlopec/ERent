package com.ua.erent.module.core.init;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * <p>
 * Performs initial app initialization process
 * </p>
 * Created by Максим on 10/14/2016.
 */
public interface InitializationManager {

    /**
     * Runs initialization process asynchronously
     *
     * @param callback callback to process
     *                 initialization progress
     */
    void initialize(@NotNull Session session, @NotNull Collection<? extends Initializeable> initializeables,
                    @NotNull IInitCallback callback);

}
