package com.ua.erent.module.core.presentation.mvp.presenter.interfaces;

import android.support.design.widget.TabLayout;

import com.ua.erent.module.core.presentation.mvp.core.IBasePresenter;
import com.ua.erent.module.core.presentation.mvp.view.InitialScreenActivity;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 10/28/2016.
 */

public abstract class IInitialScreenPresenter extends IBasePresenter<InitialScreenActivity> {

    public abstract void onTabSelected(@NotNull TabLayout.Tab tab);

}
