package com.ua.erent.module.core.account.auth.di;

import com.ua.erent.module.core.account.auth.user.api.IUserProvider;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Максим on 11/4/2016.
 */
@Singleton
@Component(modules = UserModule.class)
public interface UserComponent {

    IUserProvider getUserProvider();

}
