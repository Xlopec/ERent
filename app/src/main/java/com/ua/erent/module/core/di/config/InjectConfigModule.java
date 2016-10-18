package com.ua.erent.module.core.di.config;

import android.app.Application;

import com.ua.erent.module.core.account.auth.component.AuthComponent;
import com.ua.erent.module.core.account.auth.component.DaggerAuthComponent;
import com.ua.erent.module.core.account.auth.module.AuthModule;
import com.ua.erent.module.core.app.component.AppComponent;
import com.ua.erent.module.core.app.component.DaggerAppComponent;
import com.ua.erent.module.core.app.module.AppModule;
import com.ua.erent.module.core.di.Injector;
import com.ua.erent.module.core.networking.module.NetworkingModule;
import com.ua.erent.module.core.presentation.mvp.component.DaggerLoginComponent;
import com.ua.erent.module.core.presentation.mvp.component.DaggerTestComponent;
import com.ua.erent.module.core.presentation.mvp.component.LoginComponent;
import com.ua.erent.module.core.presentation.mvp.component.TestComponent;
import com.ua.erent.module.core.presentation.mvp.module.LoginModule;
import com.ua.erent.module.core.presentation.mvp.module.TestModule;

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
        /*
        * Be careful while adding your components. Note that
        * each component should 'live' as long as context does!
        * */
        final AppModule appModule = new AppModule(app);
        final NetworkingModule networkingModule = new NetworkingModule(retrofit);
        final AuthModule authModule = new AuthModule();
        final LoginModule loginModule = new LoginModule(app);
        // global scope component
        final AppComponent appComponent = DaggerAppComponent.builder().appModule(appModule).build();

        ///final NetworkingComponent networkingComponent = DaggerNetworkingComponent.builder().
         //       networkingModule(networkingModule).build();
        final AuthComponent authComponent = DaggerAuthComponent.builder().authModule(authModule).
                appModule(appModule).networkingModule(networkingModule).build();

        injector
                .registerComponentFactory(TestComponent.class, () -> DaggerTestComponent.builder().appComponent(appComponent).
                        testModule(new TestModule()).build())
                .registerComponentFactory(LoginComponent.class, () -> DaggerLoginComponent.builder().
                        authComponent(authComponent).loginModule(loginModule).build());
    }

}
