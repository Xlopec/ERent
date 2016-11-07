package com.ua.erent.module.core.item.di;

import com.ua.erent.module.core.networking.module.NetworkingModule;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by Максим on 11/7/2016.
 */
@Singleton
@Component(modules = NetworkingModule.class)
public interface SyncServiceComponent {

    Retrofit getRetrofit();

}
