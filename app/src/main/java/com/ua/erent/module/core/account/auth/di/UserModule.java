package com.ua.erent.module.core.account.auth.di;

import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.account.auth.user.api.IUserProvider;
import com.ua.erent.module.core.account.auth.user.api.UserProvider;
import com.ua.erent.module.core.account.auth.user.domain.IUserAppService;
import com.ua.erent.module.core.account.auth.user.domain.IUserDomain;
import com.ua.erent.module.core.account.auth.user.domain.User;
import com.ua.erent.module.core.account.auth.user.domain.UserAppService;
import com.ua.erent.module.core.account.auth.user.domain.UserDomain;
import com.ua.erent.module.core.account.auth.user.domain.storage.UserStorage;
import com.ua.erent.module.core.app.domain.interfaces.IAppLifecycleManager;
import com.ua.erent.module.core.storage.DatabaseHelper;
import com.ua.erent.module.core.storage.ISingleItemStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Максим on 11/4/2016.
 */
@Module
public final class UserModule {

    @Provides
    @Singleton
    IUserProvider providerUserProvider(Retrofit retrofit) {
        return new UserProvider(retrofit);
    }

    @Provides
    @Singleton
    ISingleItemStorage<User> provideUserStorage(DatabaseHelper helper) {
        return new UserStorage(helper);
    }

    @Provides
    @Singleton
    IUserDomain provideDomainService(IUserProvider userProvider, IAuthAppService authAppService,
                                     IAppLifecycleManager lifecycleManager, ISingleItemStorage<User> storage) {
        return new UserDomain(userProvider, authAppService, lifecycleManager, storage);
    }

    @Provides
    @Singleton
    IUserAppService provideAppService(IUserDomain domain) {
        return new UserAppService(domain);
    }

}
