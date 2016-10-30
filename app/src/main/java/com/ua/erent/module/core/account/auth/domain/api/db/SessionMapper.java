package com.ua.erent.module.core.account.auth.domain.api.db;

import com.ua.erent.module.core.account.auth.bo.Session;

import org.jetbrains.annotations.NotNull;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 10/30/2016.
 */

public final class SessionMapper {

    private SessionMapper() {
        throw new RuntimeException();
    }

    public static Session toSession(@NotNull SessionPO po) {
        Preconditions.checkNotNull(po);
        return new Session(po.getLogin(), po.getToken(), po.getTokenType(), po.getId());
    }

    public static SessionPO toPersistenceObject(@NotNull Session session) {
        Preconditions.checkNotNull(session);
        return new SessionPO(session.getUserId(), session.getLogin(), session.getToken(), session.getTokenType());
    }

}
