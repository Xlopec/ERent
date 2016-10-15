package com.ua.erent.module.core.account.auth.domain.session;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.util.IObserver;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

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
    public void addSessionObserver(@NotNull IObserver<Session> observer) {
        storage.addSessionObserver(observer);
    }

    @Override
    public void removeSessionObserver(@NotNull IObserver<Session> observer) {
        storage.removeSessionObserver(observer);
    }

    @Override
    public boolean isSessionExpired() {
        return getSession() == null || getSession().isExpired();
    }

    @Override
    public Session getSession() {
        return storage.getSession();
    }

}
