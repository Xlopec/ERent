package com.ua.erent.module.core.di.config;

import com.ua.erent.module.core.config.IConfigModule;
import com.ua.erent.module.core.di.Injector;
import com.ua.erent.module.core.util.IBuilder;

import dagger.internal.Preconditions;

/**
 * <p>
 *     Contains dependency injection configuration
 * </p>
 * Created by Максим on 10/12/2016.
 */
public final class InjectionConfigModule extends IConfigModule < Void >  {

    private final IBuilder<InjectConfigModule> builder;
    private final boolean isDebugMode;

    public InjectionConfigModule(boolean isDebugMode, IBuilder<InjectConfigModule> builder) {
        this.builder = Preconditions.checkNotNull(builder);
        this.isDebugMode = isDebugMode;
    }

    @Override
    public Void configure() {
        // Dependency injection configuration
        Injector.initialize(isDebugMode).addConfig(builder.build());
        return null;
    }
}
