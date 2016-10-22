package com.ua.erent.module.core.presentation.mvp.model;

import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.account.auth.domain.ILoginCallback;
import com.ua.erent.module.core.account.auth.vo.Credentials;

import javax.inject.Inject;

/**
 * Created by Максим on 10/15/2016.
 */

public class LoginModel implements ILoginModel {

    private final IAuthAppService authHandler;

    @Inject
    public LoginModel(IAuthAppService authHandler) {
        this.authHandler = authHandler;
    }

    @Override
    public void login(Credentials credentials, ILoginCallback callback) {
        authHandler.login(credentials, callback);
    }

}
