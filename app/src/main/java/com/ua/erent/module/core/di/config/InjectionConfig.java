package com.ua.erent.module.core.di.config;

import android.app.Application;

import com.ua.erent.BuildConfig;
import com.ua.erent.module.core.config.IConfigModule;
import com.ua.erent.module.core.di.Injector;
import com.ua.erent.TestConfigModule;
import com.ua.erent.module.core.util.IBuilder;

import org.jetbrains.annotations.NotNull;

import retrofit2.Retrofit;

/**
 * <p>
 *     Contains dependency injection configuration
 * </p>
 * Created by Максим on 10/12/2016.
 */
public final class InjectionConfig extends IConfigModule < Void >  {

    private final IConfigModule<Retrofit> retrofit;

    public static class Builder implements IBuilder <InjectionConfig> {

        private IConfigModule<Retrofit> retrofitModule;

        public IConfigModule<Retrofit> getRetrofitModule() {
            return retrofitModule;
        }

        public Builder setRetrofitModule(IConfigModule<Retrofit> retrofitModule) {
            this.retrofitModule = retrofitModule;
            return this;
        }

        @Override
        public InjectionConfig build() {
            return new InjectionConfig(this);
        }
    }

    private InjectionConfig(Builder builder) {

        if(builder == null)
            throw new NullPointerException();

        if(builder.getRetrofitModule() == null)
            throw new NullPointerException();

        this.retrofit = builder.getRetrofitModule();
    }

    @Override
    public Void configure(@NotNull Application application) {
        // Dependency injection configuration
        Injector.initialize(BuildConfig.DEBUG)
                // your config goes here
                .addConfig(new TestConfigModule(retrofit.configure(application), application));
        return null;
    }
}
