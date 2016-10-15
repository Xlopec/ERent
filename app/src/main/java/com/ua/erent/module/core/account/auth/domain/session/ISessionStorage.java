package com.ua.erent.module.core.account.auth.domain.session;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.util.IObserver;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 10/14/2016.
 */

public interface ISessionStorage {

    void store(@NotNull Session session);

    void clear();

    boolean hasSession();

    Session getSession();

    void addSessionObserver(@NotNull IObserver<Session> observer);

    void removeSessionObserver(@NotNull IObserver<Session> observer);

}
