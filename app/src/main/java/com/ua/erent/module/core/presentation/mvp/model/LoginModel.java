package com.ua.erent.module.core.presentation.mvp.model;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.domain.IAuthHandler;
import com.ua.erent.module.core.account.auth.dto.Credentials;
import com.ua.erent.module.core.util.IRetrieveCallback;

import javax.inject.Inject;

/**
 * Created by Максим on 10/15/2016.
 */

public class LoginModel implements ILoginModel {

    private final IAuthHandler authHandler;

    @Inject
    public LoginModel(IAuthHandler authHandler) {
        this.authHandler = authHandler;
    }

    @Override
    public void login(Credentials credentials, IRetrieveCallback<Session> callback) {
        authHandler.loginAsync(credentials, callback);
    }

}
