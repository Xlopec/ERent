package com.ua.erent.module.core.presentation.mvp.presenter;

import android.os.Bundle;

import com.ua.erent.module.core.presentation.mvp.presenter.ITestPresenter;
import com.ua.erent.module.core.presentation.mvp.view.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Максим on 10/14/2016.
 */

public class TestPresenter extends ITestPresenter {

    @Override
    protected void onViewAttached(@NotNull MainActivity view, @Nullable Bundle savedState, @Nullable Bundle data) {

    }

    @Override
    protected void onDestroyed() {

    }
}
