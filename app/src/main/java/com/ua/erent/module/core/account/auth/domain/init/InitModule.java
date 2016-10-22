package com.ua.erent.module.core.account.auth.domain.init;

import com.ua.erent.module.core.util.Initializeable;

import java.util.Collection;
import java.util.Collections;

import dagger.Module;
import dagger.Provides;
import dagger.internal.Preconditions;

/**
 * Created by Максим on 10/22/2016.
 */
@Module
public final class InitModule {

    private final Collection<? extends Initializeable> initializeables;

    public InitModule(Collection<? extends Initializeable> initializeables) {
        this.initializeables = Collections.unmodifiableCollection(Preconditions.checkNotNull(initializeables));
    }

    @Provides
    InitializationManager provideInitManager() {
        return new InitializationManagerImp();
    }

    @Provides
    Collection<? extends Initializeable> provideInitModules() {
        return initializeables;
    }

}
