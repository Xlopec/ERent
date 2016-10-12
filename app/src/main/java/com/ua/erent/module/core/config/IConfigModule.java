package com.ua.erent.module.core.config;

import android.app.Application;

import org.jetbrains.annotations.NotNull;

/**
 * <p>
 *     Class which represents independent config module
 * </p>
 * Created by Максим on 10/12/2016.
 */
public abstract class IConfigModule < T > {
    /**
     * Subclasses should implement this method to provide their own
     * configuration
     * @param application app instance
     * */
    public abstract T configure(@NotNull  Application application);

}