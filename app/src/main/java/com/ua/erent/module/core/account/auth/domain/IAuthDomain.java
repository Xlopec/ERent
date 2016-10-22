package com.ua.erent.module.core.account.auth.domain;

import com.ua.erent.module.core.account.auth.vo.Credentials;

import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * Contains authorization business-logic
 * </p>
 * Created by Максим on 10/14/2016.
 */

public interface IAuthDomain {
    /**
     * Completes login procedure
     *
     * @param credentials credentials to login with
     * @param callback    callback to observe result
     */
    void login(@NotNull Credentials credentials, @NotNull ILoginCallback callback);

    /**
     * Completes logout procedure. After calling this method
     * a login screen will be open
     */
    void logout();

}
