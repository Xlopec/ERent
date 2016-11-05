package com.ua.erent.module.core.init.domain;

import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.init.IInitCallback;
import com.ua.erent.module.core.init.domain.delegate.AbstractInitDelegate;
import com.ua.erent.module.core.init.domain.delegate.InitDelegates;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import dagger.internal.Preconditions;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Максим on 10/22/2016.
 */

public final class InitializationManagerImp implements InitializationManager {

    public InitializationManagerImp() {
    }

    @Override
    public void initialize(@NotNull Session session, @NotNull Collection<? extends Initializeable> initializeables,
                           @NotNull IInitCallback callback) {

        Preconditions.checkNotNull(session);
        Preconditions.checkNotNull(initializeables);
        Preconditions.checkNotNull(callback);

        if (initializeables.isEmpty()) {
            callback.onInitialized();
            return;
        }
        // delegate all work to a special handler
        final AbstractInitDelegate delegate = InitDelegates.createDelegate(callback, initializeables);

        for (final Initializeable initializeable : initializeables) {
            // run initialization process
            initializeable.initialize(session)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            delegate::handleInitialized,
                            err -> delegate.handleException(initializeable, err)
                    );
        }
    }
}
