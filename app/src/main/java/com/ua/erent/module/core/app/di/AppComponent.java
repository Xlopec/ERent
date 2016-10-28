package com.ua.erent.module.core.app.di;

import android.app.Application;

import com.ua.erent.module.core.app.domain.interfaces.IAppLifecycleManager;
import com.ua.erent.module.core.app.domain.interfaces.IAppService;
import com.ua.erent.trash.ISomeAppService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Максим on 10/11/2016.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    Application getApplication();

    ISomeAppService getSomeAppService();

    IAppLifecycleManager getAppLifecycleManager();

    IAppService getAppService();

}
