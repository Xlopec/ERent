package com.ua.erent.module.core.app;

import android.app.Application;

import com.ua.erent.BuildConfig;
import com.ua.erent.module.core.account.auth.di.AuthComponent;
import com.ua.erent.module.core.account.auth.di.AuthModule;
import com.ua.erent.module.core.account.auth.di.DaggerAuthComponent;
import com.ua.erent.module.core.account.auth.di.InitModule;
import com.ua.erent.module.core.app.component.AppComponent;
import com.ua.erent.module.core.app.component.DaggerAppComponent;
import com.ua.erent.module.core.app.module.AppModule;
import com.ua.erent.module.core.config.AbstractConfigComposer;
import com.ua.erent.module.core.di.Injector;
import com.ua.erent.module.core.di.config.InjectConfigModule;
import com.ua.erent.module.core.networking.component.DaggerBaseNetworkingComponent;
import com.ua.erent.module.core.networking.config.RetrofitConfigModule;
import com.ua.erent.module.core.networking.module.BaseNetworkingModule;
import com.ua.erent.module.core.networking.module.NetworkingModule;
import com.ua.erent.module.core.presentation.mvp.module.LoginModule;
import com.ua.erent.module.core.presentation.mvp.view.LoginActivity;
import com.ua.erent.module.core.util.IBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import dagger.internal.Preconditions;
import retrofit2.Retrofit;

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
        this.application = Preconditions.checkNotNull(application);
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
        // main dependency modules
        final AppModule appModule = new AppModule(application);
        final BaseNetworkingModule baseNetworkingModule = new BaseNetworkingModule();
        final AuthModule authModule = new AuthModule(LoginActivity.class);
        final LoginModule loginModule = new LoginModule(application);

        final RetrofitConfigModule.Builder retrofitBuilder = new RetrofitConfigModule.Builder();
        final Retrofit retrofit = retrofitBuilder.setInterceptService(DaggerBaseNetworkingComponent.builder().
                baseNetworkingModule(baseNetworkingModule).build().getInterceptService()).build()
                .configure();

        final NetworkingModule networkingModule = new NetworkingModule(retrofit);
        final AppComponent appComponent = DaggerAppComponent.builder().appModule(appModule).build();

        final InitModule initModule = new InitModule(
                Collections.singletonList(appComponent.getAppService())
        );

        final AuthComponent authComponent = DaggerAuthComponent.builder().authModule(authModule).
                appModule(appModule).networkingModule(networkingModule).initModule(initModule).build();

        // register target dependency inject modules
        final IBuilder<InjectConfigModule> injectModuleBuilder = new InjectConfigModule.Builder()
                .setLoginModule(loginModule).setAppComponent(appComponent).setAuthComponent(authComponent);

        Injector.initialize(BuildConfig.DEBUG).addConfig(injectModuleBuilder.build());
    }
}