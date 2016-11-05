package com.ua.erent.module.core.account.auth.domain;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.vo.SignInCredentials;
import com.ua.erent.module.core.account.auth.vo.SignUpCredentials;
import com.ua.erent.module.core.init.IInitCallback;
import com.ua.erent.module.core.networking.service.IPacketInterceptService;
import com.ua.erent.module.core.networking.util.HTTP_CODE;
import com.ua.erent.module.core.storage.ISingleItemStorage;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by Максим on 10/15/2016.
 */
@Singleton
public final class AuthAppService implements IAuthAppService {

    private final IAuthDomain domain;
    private final ISingleItemStorage<Session> storage;
    private final Collection<Initializeable> initializeables;

    static int i = 0;

    @Inject
    public AuthAppService(IPacketInterceptService interceptService, ISingleItemStorage<Session> storage,
                          IAuthDomain domain) {

        if(i != 0)
            throw new RuntimeException();
        i++;
        this.storage = storage;
        this.domain = domain;
        this.initializeables = new ArrayList<>();

        interceptService.addResponseObserver(response -> {

            final HTTP_CODE httpCode = HTTP_CODE.fromHttpCode(response.code());

            if (httpCode == HTTP_CODE.UNAUTHORIZED) {
                // token expired
                logout();
            }
        });
    }

    @Override
    public void login(@NotNull SignInCredentials credentials, @NotNull IInitCallback callback) {
        domain.signIn(credentials, initializeables, callback);
    }

    @Override
    public void login(@NotNull IInitCallback callback) {
        domain.signIn(initializeables, callback);
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
        return domain.getSessionChangedObservable();
    }

    @Override
    public boolean isSessionAlive() {
        final Session session = getSession();
        return session != null && !session.isExpired();
    }

    @Override
    public Session getSession() {
        return storage.getItem();
    }

    @Override
    public void registerInitializeable(@NotNull Initializeable initializeable) {
        initializeables.add(initializeable);
    }

}
