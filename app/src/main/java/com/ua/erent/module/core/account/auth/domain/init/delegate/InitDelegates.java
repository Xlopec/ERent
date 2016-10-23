package com.ua.erent.module.core.account.auth.domain.init.delegate;

import com.ua.erent.module.core.account.auth.domain.ILoginCallback;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by Максим on 10/22/2016.
 */

public final class InitDelegates {

    private InitDelegates() {
    }

    /**
     * Creates default init delegate
     *
     * @param loginCallback
     * @param initializeables @return
     */
    public static AbstractInitDelegate createDelegate(@NotNull ILoginCallback loginCallback, @NotNull Collection<? extends Initializeable> initializeables) {
        return new InitDelegateImp(loginCallback, initializeables);
    }

}
