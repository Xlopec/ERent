package com.ua.erent.module.core.account.auth.domain;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.dto.Credentials;
import com.ua.erent.module.core.util.IRetrieveCallback;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

/**
 * Created by Максим on 10/15/2016.
 */

public final class AuthHandler implements IAuthHandler {

    private final IAuthDomain domain;

    @Inject
    public AuthHandler(IAuthDomain domain) {
        this.domain = domain;
    }

    @Override
    public void loginAsync(@NotNull Credentials credentials, @NotNull IRetrieveCallback<Session> callback) {
        domain.loginAsync(credentials, callback);
    }

    @Override
    public void logout() {
        domain.logout();
    }

}
