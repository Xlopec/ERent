package com.ua.erent;

import android.app.Application;

import com.ua.erent.module.core.app.component.AppComponent;
import com.ua.erent.module.core.app.component.DaggerAppComponent;
import com.ua.erent.module.core.app.module.AppModule;
import com.ua.erent.module.core.app.provider.AppProvider;
import com.ua.erent.module.core.di.Injector;
import com.ua.erent.module.core.networking.component.DaggerNetworkingComponent;
import com.ua.erent.module.core.networking.component.NetworkingComponent;
import com.ua.erent.module.core.networking.module.NetworkingModule;
import com.ua.erent.module.core.networking.provider.NetworkingProvider;

import org.jetbrains.annotations.NotNull;

import retrofit2.Retrofit;

/**
 * Created by Максим on 10/11/2016.
 */
public final class TestConfigModule extends Injector.IConfigModule {

    private final Application app;
    private final Retrofit retrofit;

    public TestConfigModule(@NotNull Retrofit retrofit, @NotNull Application app) {
        this.app = app;
        this.retrofit = retrofit;
    }

    @Override
    protected void configure(@NotNull Injector injector) {

        final AppModule module = new AppModule(app);
        final NetworkingModule networkingModule = new NetworkingModule(retrofit);
        final AppComponent appComponent = DaggerAppComponent.builder().appModule(module).build();
        final NetworkingComponent networkingComponent = DaggerNetworkingComponent.builder().
                networkingModule(networkingModule).build();

        injector
                .registerComponentFactory(AppComponent.class, () -> new AppProvider(appComponent))
                .registerComponentFactory(TestComponent.class, () -> new TestProvider(appComponent))
                .registerComponentFactory(NetworkingComponent.class, () -> new NetworkingProvider(networkingComponent));
    }
}
