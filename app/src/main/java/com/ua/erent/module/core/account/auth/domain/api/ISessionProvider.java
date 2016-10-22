package com.ua.erent.module.core.account.auth.domain.api;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.vo.Credentials;

import org.jetbrains.annotations.NotNull;

import rx.Observable;

/**
 * <p>
 * Represents provider which interacts with server api
 * </p>
 * Created by Максим on 10/15/2016.
 */

public interface ISessionProvider {

    /**
     * Retrieves session token asynchronously from the server and converts it to
     * {@linkplain Session} instance
     *
     * @param credentials credentials to use
     * @return rx observable to handle result
     */
    Observable<Session> fetchSession(@NotNull Credentials credentials);

}
