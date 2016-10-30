package com.ua.erent.module.core.presentation.mvp.presenter;

import android.os.Bundle;
import android.support.design.widget.TabLayout;

import com.ua.erent.module.core.presentation.mvp.model.interfaces.IInitialScreenModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IInitialScreenPresenter;
import com.ua.erent.module.core.presentation.mvp.view.InitialScreenActivity;
import com.ua.erent.module.core.presentation.mvp.view.LoginFragment;
import com.ua.erent.module.core.presentation.mvp.view.RegisterFragment;
import com.ua.erent.module.core.presentation.mvp.view.util.ViewPagerAdapter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

/**
 * Created by Максим on 10/28/2016.
 */

public final class InitialScreenPresenter extends IInitialScreenPresenter {

    private final IInitialScreenModel model;

    private ViewPagerAdapter pagerAdapter;

    @Inject
    public InitialScreenPresenter(IInitialScreenModel model) {
        this.model = model;
    }

    @Override
    protected void onViewAttached(@NotNull InitialScreenActivity view, @Nullable Bundle savedState, @Nullable Bundle data) {

        if(pagerAdapter == null) {
            pagerAdapter = new ViewPagerAdapter(view.getSupportFragmentManager());
            pagerAdapter.addFragment(new LoginFragment());
            pagerAdapter.addFragment(new RegisterFragment());
        }

        if(isFirstTimeAttached()) {
            view.setViewPagerAdapter(pagerAdapter);
        }
    }

    @Override
    protected void onDestroyed() {
        pagerAdapter = null;
    }

    @Override
    public void onTabSelected(@NotNull TabLayout.Tab tab) {
        getView().hideKeyboard();
        getView().setHomeButtonEnabled(tab.getPosition() != 0);
    }
}
