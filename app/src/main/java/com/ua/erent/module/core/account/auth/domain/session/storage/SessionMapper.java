package com.ua.erent.module.core.account.auth.domain.session.storage;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.vo.UserID;

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
        return new Session(new UserID(po.getId()), po.getToken(), po.getLogin(), po.getTokenType());
    }

    public static SessionPO toPersistenceObject(@NotNull Session session) {
        Preconditions.checkNotNull(session);
        return new SessionPO(session.getUserId().getId(), session.getUsername(), session.getToken(), session.getTokenType());
    }

}
