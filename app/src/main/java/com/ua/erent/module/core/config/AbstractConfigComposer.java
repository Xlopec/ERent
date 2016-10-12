package com.ua.erent.module.core.config;

import android.app.Application;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * <p>
 * Manages configuration modules
 * </p>
 * Created by Максим on 10/12/2016.
 */
public abstract class AbstractConfigComposer {

    private final Collection<IConfigModule> configurables;

    protected AbstractConfigComposer() {
        this.configurables = new ArrayList<>();
    }

    public abstract void registerModules();

    public final void configure(@NotNull Application app) {
        for (final IConfigModule config : configurables) {
            config.configure(app);
        }
    }

    public final AbstractConfigComposer addModule(@NotNull IConfigModule module) {
        configurables.add(module);
        return this;
    }

    public final AbstractConfigComposer addModule(@NotNull Collection<IConfigModule> modules) {
        configurables.addAll(modules);
        return this;
    }

    public final AbstractConfigComposer removeModule(@NotNull IConfigModule module) {
        configurables.remove(module);
        return this;
    }

    public final Collection<IConfigModule> getRegisteredModules() {
        return Collections.unmodifiableCollection(configurables);
    }

}
