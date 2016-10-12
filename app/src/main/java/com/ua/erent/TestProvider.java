package com.ua.erent;

import com.ua.erent.module.core.app.component.AppComponent;

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
