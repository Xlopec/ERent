package com.ua.erent.module.core.app;

import android.app.Application;

import com.ua.erent.BuildConfig;
import com.ua.erent.module.core.account.auth.di.AuthModule;
import com.ua.erent.module.core.account.auth.di.UserModule;
import com.ua.erent.module.core.config.AbstractConfigComposer;
import com.ua.erent.module.core.di.Injector;
import com.ua.erent.module.core.di.config.InjectConfigModule;
import com.ua.erent.module.core.init.InitModule;
import com.ua.erent.module.core.item.di.DaggerSyncServiceComponent;
import com.ua.erent.module.core.item.di.ItemModule;
import com.ua.erent.module.core.item.di.SyncModule;
import com.ua.erent.module.core.item.di.SyncServiceComponent;
import com.ua.erent.module.core.item.domain.di.CategoryModule;
import com.ua.erent.module.core.networking.component.DaggerBaseNetworkingComponent;
import com.ua.erent.module.core.networking.config.RetrofitConfigModule;
import com.ua.erent.module.core.networking.module.BaseNetworkingModule;
import com.ua.erent.module.core.networking.module.NetworkingModule;
import com.ua.erent.module.core.presentation.mvp.view.InitialScreenActivity;
import com.ua.erent.module.core.util.IBuilder;

import org.jetbrains.annotations.NotNull;

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
        final BaseNetworkingModule baseNetworkingModule = new BaseNetworkingModule();
        final RetrofitConfigModule.Builder retrofitBuilder = new RetrofitConfigModule.Builder();
        final Retrofit retrofit = retrofitBuilder.setInterceptService(DaggerBaseNetworkingComponent.builder().
                baseNetworkingModule(baseNetworkingModule).build().getInterceptService()).build().configure();

        final NetworkingModule networkingModule = new NetworkingModule(retrofit);
        final AppComponent appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(application))
                .initModule(new InitModule())
                .authModule(new AuthModule(InitialScreenActivity.class))
                .baseNetworkingModule(baseNetworkingModule)
                .networkingModule(networkingModule)
                .userModule(new UserModule())
                .itemModule(new ItemModule())
                .categoryModule(new CategoryModule())
                .build();

        final SyncServiceComponent syncServiceComponent = DaggerSyncServiceComponent.builder()
                .syncModule(new SyncModule(retrofit)).build();

        appComponent.getInitService().registerInitializeable(appComponent.getInitTargets());
        // signUp target dependency inject modules
        final IBuilder<InjectConfigModule> injectModuleBuilder = new InjectConfigModule.Builder()
                .setAppComponent(appComponent).setSyncServiceComponent(syncServiceComponent);

        Injector.initialize(BuildConfig.DEBUG).addConfig(injectModuleBuilder.build());
    }
}