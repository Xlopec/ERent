package com.ua.erent.module.core.di.component;

import android.app.Application;

import com.ua.erent.IAppService;
import com.ua.erent.module.core.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Максим on 10/11/2016.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    Application getApplication();

    IAppService getAppService();

}
