package com.ua.erent.module.core.di;

import com.ua.erent.module.core.di.component.AppComponent;
import com.ua.erent.module.core.di.component.DaggerTestComponent;
import com.ua.erent.module.core.di.component.TestComponent;
import com.ua.erent.module.core.di.module.TestModule;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by Максим on 10/11/2016.
 */
public class TestProvider implements Provider<TestComponent> {

    private final TestComponent testComponent;

    @Inject
    public TestProvider(AppComponent appComponent) {
        testComponent = DaggerTestComponent.builder().appComponent(appComponent).testModule(new TestModule()).build();
    }

    @Override
    public TestComponent get() {
        return testComponent;
    }
}
