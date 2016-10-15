package com.ua.erent.module.core.presentation.mvp.presenter;

import com.ua.erent.module.core.presentation.mvp.core.IBasePresenter;
import com.ua.erent.module.core.presentation.mvp.view.LoginActivity;

/**
 * Created by Максим on 10/15/2016.
 */

public abstract class ILoginPresenter extends IBasePresenter<LoginActivity> {

    public abstract void onLogin(String login, String password);

}
