package com.ua.erent.module.core.presentation.mvp.presenter.interfaces;

import com.ua.erent.module.core.presentation.mvp.core.IBasePresenter;
import com.ua.erent.module.core.presentation.mvp.view.MyItemsActivity;

/**
 * Created by Максим on 12/11/2016.
 */

public abstract class IMyItemsPresenter extends IBasePresenter<MyItemsActivity> {

    public abstract void onLoadNext();

    public abstract void onLoadPrev();

    public abstract void onCreateNew();

    public abstract void onBackButton();

    public abstract void onRefresh();
}
