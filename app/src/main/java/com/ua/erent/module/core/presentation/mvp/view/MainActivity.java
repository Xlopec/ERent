package com.ua.erent.module.core.presentation.mvp.view;

import android.content.Context;
import android.os.Bundle;

import com.ua.erent.R;
import com.ua.erent.module.core.presentation.mvp.component.TestComponent;
import com.ua.erent.module.core.presentation.mvp.core.IBaseView;
import com.ua.erent.module.core.presentation.mvp.core.InjectableActivity;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ITestPresenter;

import org.jetbrains.annotations.NotNull;

public final class MainActivity extends InjectableActivity<MainActivity, ITestPresenter> implements IBaseView {

    public MainActivity() {
        super(R.layout.activity_main, TestComponent.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NotNull
    @Override
    public Context getContext() {
        return this;
    }

}
