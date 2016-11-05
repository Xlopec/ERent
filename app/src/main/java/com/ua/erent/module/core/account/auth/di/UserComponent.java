package com.ua.erent.module.core.account.auth.di;

import com.ua.erent.module.core.account.auth.user.domain.IUserAppService;
import com.ua.erent.module.core.account.auth.user.domain.IUserDomain;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Максим on 11/4/2016.
 */
@Singleton
@Component(dependencies = {AuthComponent.class}, modules = {UserModule.class})
public interface UserComponent {

    IUserAppService getAppService();

    IUserDomain getDomain();

}
