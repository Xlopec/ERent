package com.ua.erent.module.core.account.auth;

import com.ua.erent.module.core.account.auth.component.AuthComponent;

import javax.inject.Provider;

/**
 * Created by Максим on 10/15/2016.
 */

public final class AuthProvider implements Provider<AuthComponent> {

    private final AuthComponent component;

    public AuthProvider(AuthComponent component) {
        this.component = component;
    }

    @Override
    public AuthComponent get() {
        return component;
    }
}
