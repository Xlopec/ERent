package com.ua.erent.module.core.account.auth.domain;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.domain.session.ISessionManager;
import com.ua.erent.module.core.account.auth.vo.SignInCredentials;
import com.ua.erent.module.core.account.auth.vo.SignUpCredentials;
import com.ua.erent.module.core.networking.service.IPacketInterceptService;
import com.ua.erent.module.core.networking.util.HTTP_CODE;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Максим on 10/15/2016.
 */

public final class AuthAppService implements IAuthAppService {

    private final IAuthDomain domain;
    private final ISessionManager sessionManager;

    @Inject
    public AuthAppService(IPacketInterceptService interceptService, ISessionManager sessionManager,
                          IAuthDomain domain) {

        this.sessionManager = sessionManager;
        this.domain = domain;

        interceptService.addResponseObserver(response -> {

            final HTTP_CODE httpCode = HTTP_CODE.fromHttpCode(response.code());

            if (httpCode == HTTP_CODE.UNAUTHORIZED) {
                // token expired
                logout();
            }
        });
    }

    @Override
    public void login(@NotNull SignInCredentials credentials, @NotNull ILoginCallback callback) {
        domain.signIn(credentials, callback);
    }

    @Override
    public void logout() {
        domain.logout();
    }

    @Override
    public Observable<Void> signUp(@NotNull SignUpCredentials credentials) {
        return domain.signUp(credentials);
    }

    @Override
    public Observable<Session> getSessionObs() {
        return sessionManager.getSessionObs();
    }

    @Override
    public Session getSession() {
        return sessionManager.getSession();
    }

}
