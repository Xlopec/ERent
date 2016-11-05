package com.ua.erent.module.core.init.domain.delegate;

import android.support.annotation.MainThread;

import com.ua.erent.module.core.init.IInitCallback;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

import dagger.internal.Preconditions;

/**
 * <p>
 *     Base class which is responsible for
 *     initialization processing
 * </p>
 * Created by Максим on 10/22/2016.
 */
public abstract class AbstractInitDelegate {

    protected final Collection<? extends Initializeable> initializeables;
    protected final IInitCallback loginCallback;

    AbstractInitDelegate(@NotNull IInitCallback loginCallback, @NotNull Collection<? extends Initializeable> initializeables) {
        this.loginCallback = Preconditions.checkNotNull(loginCallback);
        this.initializeables = Collections.unmodifiableCollection(initializeables);
    }

    @MainThread
    public void onPreExecute() {
        loginCallback.onPreExecute();
    }

    @MainThread
    public abstract void handleInitialized(@NotNull Initializeable initializeable);

    @MainThread
    public abstract void handleException(@NotNull Initializeable initializeable, @NotNull Throwable th);

}
