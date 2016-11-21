package com.ua.erent.module.core.app.domain;

import com.ua.erent.module.core.app.domain.interfaces.IAppInitManager;
import com.ua.erent.module.core.app.domain.interfaces.IAppLifecycleManager;
import com.ua.erent.module.core.app.domain.interfaces.IAppService;

import javax.inject.Inject;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 10/28/2016.
 */

public final class AppService implements IAppService {

    private final IAppInitManager initManager;
    private final IAppLifecycleManager lifecycleManager;

    @Inject
    public AppService(IAppLifecycleManager lifecycleManager, IAppInitManager initManager) {
        this.lifecycleManager = Preconditions.checkNotNull(lifecycleManager);
        this.initManager = initManager;
    }

    @Override
    public IAppLifecycleManager getStateCallbackManager() {
        return lifecycleManager;
    }

    @Override
    public void initialize() {
        initManager.initialize();
    }
}
