package com.ua.erent.module.core.presentation.mvp.presenter.interfaces;

import com.ua.erent.module.core.presentation.mvp.view.CategoriesActivity;
import com.ua.erent.module.core.presentation.mvp.core.IBasePresenter;

/**
 * Created by Максим on 10/14/2016.
 */

public abstract class IMainPresenter extends IBasePresenter<CategoriesActivity> {

    public abstract void onLogout();

}
