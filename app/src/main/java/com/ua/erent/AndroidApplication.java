package com.ua.erent;

import android.app.Application;

import com.ua.erent.module.core.di.ConfigModule;
import com.ua.erent.module.core.di.Injector;

/**
 * Created by Максим on 10/9/2016.
 */
public class AndroidApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Injector.injector().addConfig(new ConfigModule(this));
    }
}
