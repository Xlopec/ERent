package com.ua.erent;

import com.ua.erent.module.core.app.component.AppComponent;
import com.ua.erent.module.core.di.scopes.PerActivity;
import com.ua.erent.module.core.mvp.IMVPComponent;

import dagger.Component;

/**
 * <p>
 * Component to provide test injections
 * </p>
 * Created by Максим on 10/11/2016.
 */
@Component(dependencies = AppComponent.class, modules = {TestModule.class})
@PerActivity
public interface TestComponent extends IMVPComponent<MainActivity, ITestPresenter> {

    ITestService getTestService();

}
