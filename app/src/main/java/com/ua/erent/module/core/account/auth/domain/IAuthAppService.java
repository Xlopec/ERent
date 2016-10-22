package com.ua.erent.module.core.account.auth.domain;

import com.ua.erent.module.core.account.auth.vo.Credentials;

import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * This class plays facade role for another app
 * components. Interaction flow should
 * go only through this interface!
 * </p>
 * Created by Максим on 10/14/2016.
 */

public interface IAuthAppService {
    /**
     * Processes login action. This action involves creating of new session, initialing
     * app components and so on
     *
     * @param credentials credentials to log with
     * @param callback    callback to handle login process
     */
    void login(@NotNull Credentials credentials, @NotNull ILoginCallback callback);

    /**
     * Processes logout action. This action involves destroying of current session,
     * stopping all running services and opening login activity
     */
    void logout();

}