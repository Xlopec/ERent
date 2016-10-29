package com.ua.erent.module.core.di;

import android.content.Context;

import com.ua.erent.module.core.di.util.ComponentFactory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

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
    private final Map<Context, Collection<?>> contextToComponent = new WeakHashMap<>();
    private final Map<Class<?>, ComponentFactory<?>> providerMap = new HashMap<>(10);

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
    public <T> Injector registerComponentFactory(Class<T> component, ComponentFactory<T> factory) {

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
     *
     * @param configModule config module instance
     * @return same instance of this injector
     */
    public Injector addConfig(IConfigModule configModule) {

        if (configModule == null)
            throw new NullPointerException("configModule == null");

        configModule.configure(this);
        return this;
    }

    /**
     * @return set of all registered components
     */
    public Set<Class<?>> getRegisteredComponents() {
        return Collections.unmodifiableSet(providerMap.keySet());
    }

    /**
     * <p>
     * Returns registered component to inject which will be attached to a given
     * context. It means that lifecycle of this component will not be longer that
     * lifecycle of given context.
     * </p>
     * <p>
     * Component should be released by invocation of {@link #destroyComponent(Context)}
     * </p>
     *
     * @param context   context for which component should be retrieved
     * @param component component's class
     * @param <T>       component's type
     * @return component instance which is attached to a specified context
     */
    @SuppressWarnings("unchecked")
    public <T> T getComponent(@NotNull Context context, @NotNull Class<T> component) {

        synchronized (contextToComponent) {

            Collection components = contextToComponent.get(context);

            if (components == null) {

                final T mComponent = getComponent(component);

                components = new ArrayList<>(1);
                components.add(mComponent);
                contextToComponent.put(context, components);
                return mComponent;
            }

            for (final Object tmpComp : components) {
                if (component.isAssignableFrom(tmpComp.getClass())) {
                    return (T) tmpComp;
                }
            }

            final T mComponent = getComponent(component);

            components.add(component);
            contextToComponent.put(context, components);
            return mComponent;
        }
    }

    /**
     * <p>
     * Detaches component from specified context, so that can be garbage collected
     * </p>
     *
     * @param context context for which components should be released
     */
    public void destroyComponent(@NotNull Context context) {

        synchronized (contextToComponent) {
            contextToComponent.remove(context);
        }
    }

    /**
     * Returns registered component by its class
     *
     * @param component component class to return
     * @param <T>       component type
     * @return component class instance
     */
    @SuppressWarnings("unchecked")
    private <T> T getComponent(Class<T> component) {

        if (component == null)
            throw new NullPointerException("class == null");

        if (isDebugMode) {
            // enable annotation check
            checkIsComponent(component);
        }
        // safe cast because we store provider factories in #providerMap
        final ComponentFactory<T> factory = (ComponentFactory<T>) providerMap.get(component);

        if (factory == null) {
            throw new RuntimeException(String.format("Unknown component class (%s), did you forget " +
                    "to signUp corresponding provider factory?", component));
        }

        return factory.create();
    }

    private static void checkIsComponent(Class<?> cl) {

        if (!cl.isAnnotationPresent(Component.class)) {
            throw new IllegalArgumentException(String.format("class isn't annotated with %s", Component.class));
        }
    }

}
