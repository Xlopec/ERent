package com.ua.erent.module.core.di.config;

import android.app.Application;

import com.ua.erent.module.core.account.auth.AuthProvider;
import com.ua.erent.module.core.account.auth.component.AuthComponent;
import com.ua.erent.module.core.account.auth.component.DaggerAuthComponent;
import com.ua.erent.module.core.account.auth.module.AuthModule;
import com.ua.erent.module.core.app.component.AppComponent;
import com.ua.erent.module.core.app.component.DaggerAppComponent;
import com.ua.erent.module.core.app.module.AppModule;
import com.ua.erent.module.core.app.provider.AppProvider;
import com.ua.erent.module.core.di.Injector;
import com.ua.erent.module.core.networking.component.DaggerNetworkingComponent;
import com.ua.erent.module.core.networking.component.NetworkingComponent;
import com.ua.erent.module.core.networking.module.NetworkingModule;
import com.ua.erent.module.core.networking.provider.NetworkingProvider;
import com.ua.erent.module.core.presentation.mvp.component.DaggerLoginComponent;
import com.ua.erent.module.core.presentation.mvp.component.LoginComponent;
import com.ua.erent.module.core.presentation.mvp.component.TestComponent;
import com.ua.erent.module.core.presentation.mvp.component.TestProvider;
import com.ua.erent.module.core.presentation.mvp.module.LoginModule;

import org.jetbrains.annotations.NotNull;

import retrofit2.Retrofit;

/**
 * Created by Максим on 10/11/2016.
 */
public final class InjectConfigModule extends Injector.IConfigModule {

    private final Application app;
    private final Retrofit retrofit;

    public InjectConfigModule(@NotNull Retrofit retrofit, @NotNull Application app) {
        this.app = app;
        this.retrofit = retrofit;
    }

    @Override
    protected void configure(@NotNull Injector injector) {

        final AppModule appModule = new AppModule(app);
        final NetworkingModule networkingModule = new NetworkingModule(retrofit);
        final AuthModule authModule = new AuthModule();
        final LoginModule loginModule = new LoginModule(app);

        final AppComponent appComponent = DaggerAppComponent.builder().appModule(appModule).build();
        final NetworkingComponent networkingComponent = DaggerNetworkingComponent.builder().
                networkingModule(networkingModule).build();
        final AuthComponent authComponent = DaggerAuthComponent.builder().authModule(authModule).
                appModule(appModule).networkingModule(networkingModule).build();
        final LoginComponent loginComponent = DaggerLoginComponent.builder().
                authComponent(authComponent).loginModule(loginModule).build();

        injector
                .registerComponentFactory(AppComponent.class, () -> new AppProvider(appComponent))
                .registerComponentFactory(TestComponent.class, () -> new TestProvider(appComponent))
                .registerComponentFactory(NetworkingComponent.class, () -> new NetworkingProvider(networkingComponent))
                .registerComponentFactory(AuthComponent.class, () -> new AuthProvider(authComponent))
                .registerComponentFactory(LoginComponent.class, () -> () -> loginComponent);
    }
}
