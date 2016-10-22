package com.ua.erent.module.core.account.auth.domain.init.delegate;

import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 10/22/2016.
 */

final class DelegateCallback implements Initializeable.ICallback {

    private final Initializeable initializeable;
    private final AbstractInitDelegate delegate;

    DelegateCallback(Initializeable initializeable, AbstractInitDelegate delegate) {
        this.delegate = delegate;
        this.initializeable = initializeable;
    }

    @Override
    public void onInitialized() {
        delegate.handleInitialized(initializeable);
    }

    @Override
    public void onException(@NotNull Throwable th) {
        delegate.handleException(initializeable, th);
    }
}
