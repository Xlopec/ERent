package com.ua.erent.module.core.account.auth.di;

import com.ua.erent.module.core.app.di.AppModule;
import com.ua.erent.module.core.networking.module.NetworkingModule;
import com.ua.erent.module.core.util.Initializeable;

import java.util.Collection;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Максим on 11/5/2016.
 */
@Singleton
@Component(modules = {InitModule.class, AuthModule.class, UserModule.class, NetworkingModule.class, AppModule.class})
public interface InitComponent {

    Collection<Initializeable> getInitTarget();

}
