package com.ua.erent.module.core.account.auth.domain.init.delegate;

import com.ua.erent.module.core.account.auth.domain.ILoginCallback;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by Максим on 10/22/2016.
 */

final class InitDelegateImp extends AbstractInitDelegate {

    private int counter;
    private boolean failed;

    InitDelegateImp(@NotNull ILoginCallback loginCallback, @NotNull Collection<? extends Initializeable> initializeables) {
        super(loginCallback, initializeables);
        counter = 0;
        failed = false;
    }

    @Override
    public void handleInitialized(@NotNull Initializeable initializeable) {

        if (!failed) {

            if(++counter > initializeables.size())
                throw new RuntimeException("expected init modules size and actual one doesn't match");

            if (counter == initializeables.size()) {
                loginCallback.onInitialized();
            } else {
                loginCallback.onComponentInitialized(initializeable, counter, initializeables.size());
            }
        }
    }

    @Override
    public void handleException(@NotNull Initializeable initializeable, @NotNull Throwable th) {

        if (initializeable.failOnException()) {

            if(!failed) {
                failed = true;
                loginCallback.onFailure(initializeable, th);

                for (final Initializeable init : initializeables) {
                    init.onReject();
                }
            }
        } else {
            loginCallback.onException(initializeable, th);
        }
    }
}
