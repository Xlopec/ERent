package com.ua.erent.module.core.account.auth.domain.session.storage;

import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.account.auth.user.domain.vo.UserID;

import org.jetbrains.annotations.NotNull;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 10/30/2016.
 */

final class SessionMapper {

    private SessionMapper() {
        throw new RuntimeException();
    }

    static Session toSession(@NotNull SessionPO po) {
        Preconditions.checkNotNull(po);
        return new Session(new UserID(po.getId()), po.getToken(), po.getLogin(), po.getTokenType());
    }

    static SessionPO toPersistenceObject(@NotNull Session session) {
        Preconditions.checkNotNull(session);
        return new SessionPO(session.getUserId().getId(), session.getUsername(), session.getRawToken(), session.getTokenType());
    }

}
