package com.ua.erent.module.core.networking.component;

import com.ua.erent.module.core.networking.module.BaseNetworkingModule;
import com.ua.erent.module.core.networking.service.IPacketInterceptService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Максим on 10/21/2016.
 */
@Component(modules = {BaseNetworkingModule.class})
@Singleton
public interface BaseNetworkingComponent {

    IPacketInterceptService getInterceptService();

}
