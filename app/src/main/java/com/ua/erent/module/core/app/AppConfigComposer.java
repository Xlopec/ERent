package com.ua.erent.module.core.app;

import android.app.Application;

import com.ua.erent.module.core.config.AbstractConfigComposer;
import com.ua.erent.module.core.di.config.InjectionConfig;
import com.ua.erent.module.core.networking.config.RetrofitConfigModule;

import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * Default singleton implementation of {@linkplain AbstractConfigComposer}
 * </p>
 * Created by Максим on 10/13/2016.
 */
final class AppConfigComposer extends AbstractConfigComposer {

    private static AbstractConfigComposer instance;

    private final Application application;

    private AppConfigComposer(@NotNull Application application) {
        this.application = application;
    }

    static AbstractConfigComposer initialize(Application application) {
        // Double Checked Locking & volatile singleton realization
        AbstractConfigComposer localInstance = instance;

        if (localInstance == null) {

            synchronized (AbstractConfigComposer.class) {

                localInstance = instance;

                if (localInstance == null) {
                    instance = localInstance = new AppConfigComposer(application);
                    localInstance.registerModules();
                }
                return localInstance;
            }
        }

        throw new IllegalStateException(String.format("%s cannot be initialized more than once!",
                AbstractConfigComposer.class.getName()));
    }

    public static AbstractConfigComposer instance() {

        if (instance == null)
            throw new IllegalStateException(String.format("Trying to use %s without initialization",
                    AbstractConfigComposer.class.getName()));

        return instance;
    }

    @Override
    public void registerModules() {

        final InjectionConfig.Builder injConfigBuilder = new InjectionConfig.Builder();

        injConfigBuilder.setRetrofitModule(new RetrofitConfigModule());

        AppConfigComposer.instance()
                .addModule(injConfigBuilder.build())
                // apply configuration
                .configure(application);
    }
}