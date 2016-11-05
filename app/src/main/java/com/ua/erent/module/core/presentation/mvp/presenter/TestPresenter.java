package com.ua.erent.module.core.presentation.mvp.presenter;

import android.os.Bundle;

import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ITestPresenter;
import com.ua.erent.module.core.presentation.mvp.view.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

/**
 * Created by Максим on 10/14/2016.
 */

public class TestPresenter extends ITestPresenter {

    private final IAuthAppService appService;

    @Inject
    public TestPresenter(IAuthAppService appService) {
        this.appService = appService;
    }

    @Override
    protected void onViewAttached(@NotNull MainActivity view, @Nullable Bundle savedState, @Nullable Bundle data) {

    }

    @Override
    protected void onDestroyed() {

    }

    @Override
    public void onLogout() {
        appService.logout();
    }
}
