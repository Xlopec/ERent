package com.ua.erent.module.core.networking.module;

import com.ua.erent.module.core.networking.service.IPacketInterceptService;
import com.ua.erent.module.core.networking.service.PacketInterceptService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 10/21/2016.
 */
@Module
public class BaseNetworkingModule {

    @Provides
    @Singleton
    IPacketInterceptService provideInterceptService() {
        return new PacketInterceptService();
    }

}
