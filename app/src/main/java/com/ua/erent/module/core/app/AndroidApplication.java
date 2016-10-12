package com.ua.erent.module.core.app;

import android.app.Application;

/**
 * <p>
 *     Main class, which represents the whole
 *     app
 * </p>
 * Created by Максим on 10/9/2016.
 */
public final class AndroidApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // initializes configuration composer
        AppConfigComposer.initialize(this);
    }
}
