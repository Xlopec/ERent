package com.ua.erent.module.core.account.auth.domain;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.domain.api.ISessionProvider;
import com.ua.erent.module.core.account.auth.domain.session.ISessionManager;
import com.ua.erent.module.core.account.auth.dto.Credentials;
import com.ua.erent.module.core.util.IRetrieveCallback;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

/**
 * Created by Максим on 10/15/2016.
 */

public final class AuthDomain implements IAuthDomain {

    private final ISessionManager sessionManager;
    private final ISessionProvider provider;

    @Inject
    public AuthDomain(ISessionManager sessionManager, ISessionProvider provider) {
        this.sessionManager = sessionManager;
        this.provider = provider;
    }

    @Override
    public void loginAsync(@NotNull Credentials credentials, @NotNull IRetrieveCallback<Session> callback) {

        provider.authAsync(credentials, new IRetrieveCallback<Session>() {

            @Override
            public void onPreExecute() {
                callback.onPreExecute();
            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }

            @Override
            public void onResult(Session session) {
                //TODO start components loading here
                sessionManager.setSession(session);
                callback.onResult(session);
            }

            @Override
            public void onException(@NotNull Exception exc) {
                callback.onException(exc);
            }

        }).execute();
    }

    @Override
    public void logout() {
        sessionManager.destroySession();
    }
}
