package com.ua.erent.module.core.account.auth.domain.init;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.domain.ILoginCallback;
import com.ua.erent.module.core.account.auth.domain.init.delegate.AbstractInitDelegate;
import com.ua.erent.module.core.account.auth.domain.init.delegate.InitDelegates;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 10/22/2016.
 */

public final class InitializationManagerImp implements InitializationManager {

    public InitializationManagerImp() {
    }

    @Override
    public void initialize(@NotNull Session session, @NotNull Collection<? extends Initializeable> initializeables,
                           @NotNull ILoginCallback callback) {

        Preconditions.checkNotNull(session);
        Preconditions.checkNotNull(initializeables);
        Preconditions.checkNotNull(callback);

        if(initializeables.isEmpty())
            throw new IllegalArgumentException("nothing to init");

        // delegate all work to a special handler
        final AbstractInitDelegate delegate = InitDelegates.createDelegate(callback, initializeables);

        callback.onPreExecute();

        for(final Initializeable initializeable : initializeables) {
            // run initialization process
            initializeable.initialize(session, InitDelegates.createCallback(delegate, initializeable));
        }
    }
}