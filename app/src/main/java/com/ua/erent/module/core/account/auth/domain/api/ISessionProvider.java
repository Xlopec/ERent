package com.ua.erent.module.core.account.auth.domain.api;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.dto.Credentials;

import org.jetbrains.annotations.NotNull;

import rx.Observable;

/**
 * Created by Максим on 10/15/2016.
 */

public interface ISessionProvider {

    Observable<Session> fetchSession(@NotNull Credentials credentials);

}
