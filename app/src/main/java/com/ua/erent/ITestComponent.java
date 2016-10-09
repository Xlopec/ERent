package com.ua.erent;

import android.app.Activity;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Максим on 10/9/2016.
 */
@Singleton
@Component(modules = {TestModule.class})
public interface ITestComponent {

    void inject(@NotNull Activity activity);

    ITestService getService();

}
