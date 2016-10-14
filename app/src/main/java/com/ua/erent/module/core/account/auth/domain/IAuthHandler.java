package com.ua.erent.module.core.account.auth.domain;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.dto.Credentials;
import com.ua.erent.module.core.util.IRetrieveCallback;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 10/14/2016.
 */

public interface IAuthHandler {

    void loginAsync(@NotNull Credentials credentials, @NotNull IRetrieveCallback<Session> callback);

    void logout();

}