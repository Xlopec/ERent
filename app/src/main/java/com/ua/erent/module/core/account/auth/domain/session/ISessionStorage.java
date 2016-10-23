package com.ua.erent.module.core.account.auth.domain.session;

import com.ua.erent.module.core.account.auth.bo.Session;

import org.jetbrains.annotations.NotNull;

import rx.Observable;

/**
 * Created by Максим on 10/14/2016.
 */

public interface ISessionStorage {

    void store(@NotNull Session session);

    void clear();

    boolean hasSession();

    Session getSession();

    Observable<Session> getSessionObs();

}
