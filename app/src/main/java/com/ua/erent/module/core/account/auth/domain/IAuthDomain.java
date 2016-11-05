package com.ua.erent.module.core.account.auth.domain;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.vo.SignInCredentials;
import com.ua.erent.module.core.account.auth.vo.SignUpCredentials;
import com.ua.erent.module.core.init.IInitCallback;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

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
    void signIn(@NotNull SignInCredentials credentials,
                @NotNull Collection<Initializeable> initializeables, @NotNull IInitCallback callback);

    /**
     * Processes sign in action. Note, that in order to use this method session
     * should be is still valid in other cases
     * {@linkplain IllegalStateException} exception will be raised
     *
     * @param callback callback to handle signIn process
     */
    void signIn(@NotNull Collection<Initializeable> initializeables, @NotNull IInitCallback callback);

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

    Observable<Session> getSessionChangedObservable();

}
