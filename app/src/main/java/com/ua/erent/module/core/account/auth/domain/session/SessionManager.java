package com.ua.erent.module.core.account.auth.domain.session;

import com.ua.erent.module.core.account.auth.bo.Session;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Максим on 10/15/2016.
 */
public final class SessionManager implements ISessionManager {

    private final ISessionStorage storage;

    @Inject
    public SessionManager(ISessionStorage storage) {
        this.storage = storage;
    }

    @Override
    public void setSession(@NotNull Session session) {
        storage.store(session);
    }

    @Override
    public void destroySession() {
        storage.clear();
    }

    @Override
    public boolean isSessionExpired() {
        return getSession() == null || getSession().isExpired();
    }

    @Override
    public Observable<Session> getSessionObs() {
        return storage.getSessionObs();
    }

    @Override
    public Session getSession() {
        return storage.getSession();
    }

}
