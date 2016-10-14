package com.ua.erent.module.core.di;

import com.ua.erent.module.core.di.util.IProviderFactory;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import dagger.Component;

/**
 * <p>
 * Configurable class to manage dependency injection
 * </p>
 * Created by Максим on 10/11/2016.
 */
public final class Injector {

    private static volatile Injector instance;

    /**
     * Initializes injector. Use debug mode flag to check
     * {@linkplain Component} in runtime
     *
     * @param isDebugMode if true, then runtime annotation check will be
     *                    enabled
     */
    public static Injector initialize(boolean isDebugMode) {
        // Double Checked Locking & volatile singleton realization
        Injector localInstance = instance;

        if (localInstance == null) {

            synchronized (Injector.class) {

                localInstance = instance;

                if (localInstance == null) {
                    instance = localInstance = new Injector(isDebugMode);
                }
                return localInstance;
            }
        }

        throw new IllegalStateException(String.format("%s cannot be initialized more than once!",
                Injector.class.getName()));
    }

    public static Injector injector() {

        if (instance == null)
            throw new IllegalStateException(String.format("Trying to use %s without initialization",
                    Injector.class.getName()));

        return instance;
    }

    private final boolean isDebugMode;
    private final Map<Class<?>, IProviderFactory<?>> providerMap = new HashMap<>(10);

    /**
     * Represents injection configuration module.
     * Pass instance of this class via Injector{@link #addConfig(IConfigModule)}
     * to apply your config
     */
    public static abstract class IConfigModule {
        /**
         * Subclasses should provide configuration by implementing this method
         *
         * @param injector injector instance to work with
         */
        protected abstract void configure(@NotNull Injector injector);

    }

    private Injector(boolean isDebugMode) {
        this.isDebugMode = isDebugMode;
    }

    /**
     * Registers factory which creates subclasses of {@linkplain javax.inject.Provider}.
     * Such providers allows to retrieve ready to use injection modules
     *
     * @param <T>       component type
     * @param component app component
     * @param factory   provider factory implementation
     */
    public <T> Injector registerComponentFactory(Class<T> component, IProviderFactory<T> factory) {

        if (component == null)
            throw new NullPointerException("class == null");

        if (factory == null)
            throw new NullPointerException("factory == null");

        if (isDebugMode) {
            // enable annotation check
            checkIsComponent(component);
        }
        providerMap.put(component, factory);
        return this;
    }

    /**
     * Configures specified config module
     * @param configModule config module instance
     * @return
     */
    public Injector addConfig(IConfigModule configModule) {

        if (configModule == null)
            throw new NullPointerException("configModule == null");

        configModule.configure(this);
        return this;
    }

    /**
     * Returns registered component by its class
     * @param component component class to return
     * @param <T> component type
     * @return component class instance
     */
    @SuppressWarnings("unchecked")
    public <T> T getComponent(Class<T> component) {

        if (component == null)
            throw new NullPointerException("class == null");

        if (isDebugMode) {
            // enable annotation check
            checkIsComponent(component);
        }
        // safe cast because we store provider factories in #providerMap
        final IProviderFactory <T> factory = (IProviderFactory<T>) providerMap.get(component);

        if (factory == null) {
            throw new RuntimeException(String.format("Unknown component class (%s), did you forget " +
                    "to register corresponding provider factory?", component));
        }

        return factory.create().get();
    }

    /**
     * @return class set of all registered components
     */
    public Set<Class<?>> getRegisteredComponents() {
        return Collections.unmodifiableSet(providerMap.keySet());
    }

    private static void checkIsComponent(Class<?> cl) {

        if (!cl.isAnnotationPresent(Component.class)) {
            throw new IllegalArgumentException(String.format("class isn't annotated with %s", Component.class));
        }
    }

}
