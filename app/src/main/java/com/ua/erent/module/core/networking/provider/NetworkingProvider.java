package com.ua.erent.module.core.networking.provider;

import com.ua.erent.module.core.networking.component.NetworkingComponent;

import javax.inject.Provider;

/**
 * Created by Максим on 10/13/2016.
 */
public final class NetworkingProvider implements Provider<NetworkingComponent> {

    private final NetworkingComponent component;

    public NetworkingProvider(NetworkingComponent networkingComponent) {
        this.component = networkingComponent;
    }

    @Override
    public NetworkingComponent get() {
        return component;
    }
}
