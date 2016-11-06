package com.ua.erent.module.core.account.auth.user.domain;

import com.ua.erent.module.core.account.auth.user.domain.vo.PasswordForm;
import com.ua.erent.module.core.account.auth.user.domain.vo.UserForm;
import com.ua.erent.module.core.account.auth.user.domain.bo.User;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import rx.Observable;

/**
 * Created by Максим on 11/4/2016.
 */

public interface IUserDomain extends Initializeable {

    /**
     * Fetches user profile from server asynchronously;
     * If specified user id is the same as user id stored
     * in session, then in case of success inner cache will be updated
     * and all observables registered via {@link #getOnUserProfileChangedObservable()}
     * will be fired
     *
     * @return rx observable to monitor operation progress
     */
    Observable<User> fetchUserProfile();

    /**
     * Updates user profile asynchronously on the server;
     * in case of success inner cache will be updated
     * and all observables registered via {@link #getOnUserProfileChangedObservable()}
     * will be fired
     *
     * @param userForm update form to apply
     * @return rx observable to monitor operation progress
     */
    Observable<User> updateUserProfile(@NotNull UserForm userForm);

    /**
     * Returns cached user instance if any. To update cache user
     * {@link #fetchUserProfile()} method
     *
     * @return instance of {@linkplain User}
     */
    @Nullable
    User getCachedProfile();

    Observable<User> getOnUserProfileChangedObservable();

    Observable<Void> changePassword(@NotNull PasswordForm form);

}
