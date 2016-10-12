package com.ua.erent.module.core.networking.component;

import com.ua.erent.module.core.networking.module.NetworkingModule;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by Максим on 10/13/2016.
 */
@Singleton
@Component(modules = {NetworkingModule.class})
public interface NetworkingComponent {

    Retrofit getRetrofit();

}
