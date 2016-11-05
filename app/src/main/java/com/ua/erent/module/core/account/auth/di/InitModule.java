package com.ua.erent.module.core.account.auth.di;

import com.ua.erent.module.core.account.auth.user.domain.IUserDomain;
import com.ua.erent.module.core.util.Initializeable;

import java.util.Arrays;
import java.util.Collection;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 10/22/2016.
 */
@Module
public final class InitModule {

    @Provides
    Collection<Initializeable> provideInitTarget(IUserDomain userDomain) {
        return Arrays.asList(userDomain);
    }

}
