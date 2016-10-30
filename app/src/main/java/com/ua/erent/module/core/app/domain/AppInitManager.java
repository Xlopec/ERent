package com.ua.erent.module.core.app.domain;

import android.app.Application;

import com.ua.erent.module.core.app.domain.interfaces.IAppInitManager;
import com.ua.erent.module.core.util.FileUtils;

import javax.inject.Inject;

/**
 * Created by Максим on 10/30/2016.
 */

public final class AppInitManager implements IAppInitManager {

    private final Application application;

    @Inject
    public AppInitManager(Application application) {
        this.application = application;
    }

    @Override
    public void initialize() {

        if(application.getExternalCacheDir() != null) {
            FileUtils.delete(application.getExternalCacheDir(), true);
        }

        FileUtils.delete(application.getCacheDir(), true);
    }
}
