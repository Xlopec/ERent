package com.ua.erent.module.core.presentation.mvp.presenter;

import android.os.Bundle;

import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IItemsPresenter;
import com.ua.erent.module.core.presentation.mvp.view.CategoriesActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

/**
 * Created by Максим on 10/14/2016.
 */

public class ItemsPresenter extends IItemsPresenter {

    private final IAuthAppService appService;

    @Inject
    public ItemsPresenter(IAuthAppService appService) {
        this.appService = appService;
    }

    @Override
    protected void onViewAttached(@NotNull CategoriesActivity view, @Nullable Bundle savedState, @Nullable Bundle data) {

    }

    @Override
    protected void onDestroyed() {

    }

}
