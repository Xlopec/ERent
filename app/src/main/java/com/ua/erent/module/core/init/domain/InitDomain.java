package com.ua.erent.module.core.init.domain;

import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.init.IInitCallback;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

/**
 * Created by Максим on 11/5/2016.
 */

public final class InitDomain implements IInitDomain {

    private final InitializationManager manager;
    private final Collection<Initializeable> initializeables;

    @Inject
    public InitDomain(InitializationManager manager) {
        this.manager = manager;
        this.initializeables = new ArrayList<>();
    }

    @Override
    public void registerInitializeable(@NotNull Collection<Initializeable> initializeables) {
        this.initializeables.addAll(initializeables);
    }

    @Override
    public void registerInitializeable(@NotNull Initializeable initializeable) {
        initializeables.add(initializeable);
    }

    @Override
    public void unregisterInitializeable(@NotNull Initializeable initializeable) {
        initializeables.remove(initializeable);
    }

    @Override
    public Collection<Initializeable> getRegisteredInitializeables() {
        return Collections.unmodifiableCollection(initializeables);
    }

    @Override
    public void initialize(@NotNull Session session, @NotNull IInitCallback callback) {
        manager.initialize(session, initializeables, callback);
    }
}
