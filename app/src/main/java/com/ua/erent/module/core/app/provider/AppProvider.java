package com.ua.erent.module.core.app.provider;

import com.ua.erent.module.core.app.component.AppComponent;

import javax.inject.Provider;

/**
 * Created by Максим on 10/11/2016.
 */
public final class AppProvider implements Provider<AppComponent> {

    private final AppComponent component;

    public AppProvider(AppComponent appComponent) {
        component = appComponent;
    }

    @Override
    public AppComponent get() {
        return component;
    }
}
