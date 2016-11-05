package com.ua.erent.module.core.account.auth.domain;

import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.account.auth.domain.vo.SignInCredentials;
import com.ua.erent.module.core.account.auth.domain.vo.SignUpCredentials;
import com.ua.erent.module.core.init.IInitCallback;

import org.jetbrains.annotations.NotNull;

import rx.Observable;

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
     * Processes signIn action. This action involves creating of new session, initialing
     * app components and so on
     *
     * @param credentials credentials to log with
     * @param callback    callback to handle signIn process
     */
    void login(@NotNull SignInCredentials credentials, @NotNull IInitCallback callback);

    /**
     * Processes sign in action. Note, that in order to use this method session
     * should be is still valid. To check its state you can call {@link #getSession()}
     * and check it yourself or use {@link #isSessionAlive()}, in other cases
     * {@linkplain IllegalStateException} exception will be raised
     *
     * @param callback callback to handle signIn process
     */
    void login(@NotNull IInitCallback callback);

    /**
     * Processes logout action. This action involves destroying of current session,
     * stopping all running services and opening signIn activity
     */
    void logout();

    Observable<Void> signUp(@NotNull SignUpCredentials credentials);

    Observable<Session> getSessionObs();

    boolean isSessionAlive();

    Session getSession();

}