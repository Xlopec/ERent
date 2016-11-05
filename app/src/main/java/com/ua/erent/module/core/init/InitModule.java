package com.ua.erent.module.core.init;

import com.ua.erent.module.core.account.auth.user.domain.IUserDomain;
import com.ua.erent.module.core.init.domain.IInitAppService;
import com.ua.erent.module.core.init.domain.IInitDomain;
import com.ua.erent.module.core.init.domain.InitAppService;
import com.ua.erent.module.core.init.domain.InitDomain;
import com.ua.erent.module.core.init.domain.InitializationManager;
import com.ua.erent.module.core.init.domain.InitializationManagerImp;
import com.ua.erent.module.core.util.Initializeable;

import java.util.Arrays;
import java.util.Collection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 11/5/2016.
 */
@Module
public final class InitModule {

    @Provides
    @Singleton
    InitializationManager provideManager() {
        return new InitializationManagerImp();
    }

    @Provides
    @Singleton
    IInitAppService provideAppService(IInitDomain domain) {
        return new InitAppService(domain);
    }

    @Provides
    @Singleton
    IInitDomain provideInitDomain(InitializationManager manager) {
        return new InitDomain(manager);
    }

    @Provides
    @Singleton
    Collection<Initializeable> provideInitTargets(IUserDomain userDomain) {
        return Arrays.asList(userDomain);
    }

}
