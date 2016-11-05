package com.ua.erent.module.core.init.domain;

import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.init.IInitCallback;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import javax.inject.Inject;

/**
 * Created by Максим on 11/5/2016.
 */

public final class InitAppService implements IInitAppService {

    private final IInitDomain domain;

    @Inject
    public InitAppService(IInitDomain domain) {
        this.domain = domain;
    }

    @Override
    public void registerInitializeable(@NotNull Collection<Initializeable> initializeables) {
        domain.registerInitializeable(initializeables);
    }

    @Override
    public void registerInitializeable(@NotNull Initializeable initializeable) {
        domain.registerInitializeable(initializeable);
    }

    @Override
    public void unregisterInitializeable(@NotNull Initializeable initializeable) {
        domain.unregisterInitializeable(initializeable);
    }

    @Override
    public Collection<Initializeable> getRegisteredInitializeables() {
        return domain.getRegisteredInitializeables();
    }

    @Override
    public void initialize(@NotNull Session session, @NotNull IInitCallback callback) {
        domain.initialize(session, callback);
    }
}
