package com.ua.erent.module.core.di;

import android.app.Application;

import com.ua.erent.module.core.di.component.AppComponent;
import com.ua.erent.module.core.di.component.DaggerAppComponent;
import com.ua.erent.module.core.di.component.TestComponent;
import com.ua.erent.module.core.di.module.AppModule;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 10/11/2016.
 */
public final class ConfigModule extends IConfigModule {

    private final Application app;

    public ConfigModule(@NotNull Application app) {
        this.app = app;
    }

    @Override
    protected void configure(@NotNull Injector injectionManager) {

        final AppModule module = new AppModule(app);
        final AppComponent appComponent = DaggerAppComponent.builder().appModule(module).build();

        injectionManager
                .registerComponentFactory(AppComponent.class, () -> new AppProvider(appComponent))
                .registerComponentFactory(TestComponent.class, () -> new TestProvider(appComponent));
    }
}
