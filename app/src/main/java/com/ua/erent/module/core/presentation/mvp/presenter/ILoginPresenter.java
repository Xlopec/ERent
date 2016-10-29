package com.ua.erent.module.core.presentation.mvp.presenter;

import com.ua.erent.module.core.presentation.mvp.core.IBasePresenter;
import com.ua.erent.module.core.presentation.mvp.view.LoginFragment;

import rx.Observable;

/**
 * Created by Максим on 10/15/2016.
 */

public abstract class ILoginPresenter extends IBasePresenter<LoginFragment> {

    public abstract Observable<String> onLogin(String login, String password);

    public abstract void onNavigateCreateAccount();

}
