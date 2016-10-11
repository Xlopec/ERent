package com.ua.erent.module.core.di;

import com.ua.erent.module.core.di.component.AppComponent;

import javax.inject.Provider;

/**
 * Created by Максим on 10/11/2016.
 */
public class AppProvider implements Provider<AppComponent> {

    private final AppComponent component;

    public AppProvider(AppComponent appComponent) {
        component = appComponent;
    }

    @Override
    public AppComponent get() {
        return component;
    }
}
