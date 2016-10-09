package com.ua.erent;

import android.app.Application;

/**
 * Created by Максим on 10/9/2016.
 */
public class AndroidApplication extends Application {

    private static ITestComponent component;

    public static ITestComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        final TestModule module = new TestModule(this);
        AndroidApplication.component = DaggerITestComponent.builder().testModule(module).build();
    }
}
