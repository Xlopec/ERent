package com.ua.erent.module.core.presentation.mvp.view.interfaces;

import android.support.v4.view.PagerAdapter;

import com.ua.erent.module.core.presentation.mvp.core.IBaseView;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 10/28/2016.
 */

public interface IInitialScreenView extends IBaseView {

    interface NavigationListener {

        void onCreateAccount();

        void onRestorePassword();

        void onLogin();

    }

    void setViewPagerAdapter(@NotNull PagerAdapter adapter);

    void setHomeButtonEnabled(boolean enabled);

    void hideKeyboard();

}
