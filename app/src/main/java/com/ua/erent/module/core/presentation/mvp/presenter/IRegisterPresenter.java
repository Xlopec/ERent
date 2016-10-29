package com.ua.erent.module.core.presentation.mvp.presenter;

import com.ua.erent.module.core.presentation.mvp.core.IBasePresenter;
import com.ua.erent.module.core.presentation.mvp.model.SignUpForm;
import com.ua.erent.module.core.presentation.mvp.view.RegisterFragment;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 10/27/2016.
 */

public abstract class IRegisterPresenter extends IBasePresenter<RegisterFragment> {

    public abstract void onNavigateLogin();

    public abstract void onCreateAccount(@NotNull SignUpForm form);

}
