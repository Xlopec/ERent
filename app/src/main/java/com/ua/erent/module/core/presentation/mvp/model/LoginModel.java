package com.ua.erent.module.core.presentation.mvp.model;

import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.account.auth.domain.ILoginCallback;
import com.ua.erent.module.core.account.auth.vo.SignInCredentials;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.ILoginModel;

import javax.inject.Inject;

/**
 * Created by Максим on 10/15/2016.
 */

public final class LoginModel implements ILoginModel {

    private final IAuthAppService authHandler;

    @Inject
    public LoginModel(IAuthAppService authHandler) {
        this.authHandler = authHandler;
    }

    @Override
    public void login(String login, String password, ILoginCallback callback) {
        authHandler.login(new SignInCredentials(login, password), callback);
    }

}
