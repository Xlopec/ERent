package com.ua.erent.module.core.presentation.mvp.component;

import com.ua.erent.trash.ITestService;
import com.ua.erent.module.core.presentation.mvp.module.TestModule;
import com.ua.erent.module.core.app.component.AppComponent;
import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.presentation.mvp.core.IMVPComponent;
import com.ua.erent.module.core.presentation.mvp.presenter.ITestPresenter;
import com.ua.erent.module.core.presentation.mvp.view.MainActivity;

import dagger.Component;

/**
 * <p>
 * Component to provide test injections
 * </p>
 * Created by Максим on 10/11/2016.
 */
@Component(dependencies = AppComponent.class, modules = {TestModule.class})
@ActivityScope
public interface TestComponent extends IMVPComponent<MainActivity, ITestPresenter> {

    ITestService getTestService();

}
