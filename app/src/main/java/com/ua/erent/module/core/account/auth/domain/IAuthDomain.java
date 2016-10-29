package com.ua.erent.module.core.account.auth.domain;

import com.ua.erent.module.core.account.auth.vo.SignInCredentials;
import com.ua.erent.module.core.account.auth.vo.SignUpCredentials;

import org.jetbrains.annotations.NotNull;

import rx.Observable;

/**
 * <p>
 * Contains authorization business-logic
 * </p>
 * Created by Максим on 10/14/2016.
 */

public interface IAuthDomain {
    /**
     * Completes signIn procedure
     *
     * @param credentials credentials to signIn with
     * @param callback    callback to observe result
     */
    void signIn(@NotNull SignInCredentials credentials, @NotNull ILoginCallback callback);

    /**
     * Completes signIn procedure
     *
     * @param credentials credentials to signIn with
     * @return rx observable to monitor registration progress
     */
    Observable<Void> signUp(@NotNull SignUpCredentials credentials);

    /**
     * Completes logout procedure. After calling this method
     * a signIn screen will be open
     */
    void logout();

}
