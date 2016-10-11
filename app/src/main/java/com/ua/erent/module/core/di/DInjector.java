package com.ua.erent.module.core.di;

import android.app.Application;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 10/11/2016.
 */

public final class DInjector {

    private static DInjector instance;

    private final Application application;

    public static void init(@NotNull Application application) {
        if(instance == null) instance = new DInjector(application);
    }

    public static DInjector getInstance() {
        return instance;
    }

    private DInjector(@NotNull Application application) {
        this.application = application;
    }

}
