package com.ua.erent.module.core.di;

import com.ua.erent.BuildConfig;
import com.ua.erent.module.core.di.util.IProviderFactory;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import dagger.Component;

/**
 * <p>
 *     Configurable class to manage dependency injection
 * </p>
 * Created by Максим on 10/11/2016.
 */
public final class Injector {

    private static Injector instance = new Injector();

    public static Injector injector() {
        return instance;
    }

    private final Map<Class<?>, IProviderFactory<?>> providerMap = new HashMap<>(10);

    <T> Injector registerComponentFactory(@NotNull Class<T> component, @NotNull IProviderFactory<T> factory) {

        if (component == null)
            throw new NullPointerException("class == null");

        if (factory == null)
            throw new NullPointerException("factory == null");

        if (BuildConfig.DEBUG) {
            // enable annotation check
            checkIsComponent(component);
        }
        providerMap.put(component, factory);
        return this;
    }

    public Injector addConfig(@NotNull IConfigModule configModule) {

        if (configModule == null)
            throw new NullPointerException("configModule == null");

        configModule.configure(this);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T getComponent(@NotNull Class<T> component) {

        if (component == null)
            throw new NullPointerException("class == null");

        if (BuildConfig.DEBUG) {
            // enable annotation check
            checkIsComponent(component);
        }
        // safe cast because we store provider factories in #providerMap
        final IProviderFactory<T> factory = (IProviderFactory<T>) providerMap.get(component);

        if(factory == null) {
            throw new RuntimeException(String.format("Unknown component class (%s), did you forget " +
                    "to register corresponding provider factory?", component));
        }

        return factory.create().get();
    }

    private static void checkIsComponent(Class<?> cl) {

        if(!cl.isAnnotationPresent(Component.class)) {
            throw new IllegalArgumentException(String.format("class isn't annotated with %s", Component.class));
        }
    }

}
