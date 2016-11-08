package com.ua.erent.module.core.networking.module;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Максим on 10/13/2016.
 */
@Module
public final class NetworkingModule {

    private final Retrofit retrofit;

    public NetworkingModule(@NotNull Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return retrofit;
    }

}
