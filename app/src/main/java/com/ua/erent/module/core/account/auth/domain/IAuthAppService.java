package com.ua.erent.module.core.account.auth.domain;

import com.ua.erent.module.core.account.auth.dto.Credentials;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 10/14/2016.
 */

public interface IAuthAppService {

    void loginAsync(@NotNull Credentials credentials, @NotNull ILoginCallback callback);

    void logout();

}