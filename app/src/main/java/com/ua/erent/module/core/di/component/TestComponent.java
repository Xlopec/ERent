package com.ua.erent.module.core.di.component;

import android.app.Activity;

import com.ua.erent.ITestService;
import com.ua.erent.module.core.di.module.TestModule;
import com.ua.erent.module.core.di.scopes.PerActivity;

import dagger.Component;

/**
 * <p>
 * Component to provide test injections
 * </p>
 * Created by Максим on 10/11/2016.
 */
@Component(dependencies = AppComponent.class, modules = {TestModule.class})
@PerActivity
public interface TestComponent {

    ITestService getTestService();

    void inject(Activity activity);

}
