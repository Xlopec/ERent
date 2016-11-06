package com.ua.erent.module.core.account.auth.user.api;

import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.account.auth.user.domain.vo.PasswordForm;
import com.ua.erent.module.core.account.auth.user.domain.vo.UserForm;
import com.ua.erent.module.core.account.auth.user.domain.bo.User;

import org.jetbrains.annotations.NotNull;

import rx.Observable;

/**
 * <p>
 *     Represents provider which interacts with server API
 * </p>
 * Created by Максим on 11/4/2016.
 */

public interface IUserProvider {

    Observable<User> fetchUserProfile(@NotNull Session session);

    Observable<Void> updateUserProfile(@NotNull Session session, @NotNull UserForm userForm);

    Observable<Void> changePassword(@NotNull Session session, @NotNull PasswordForm form);

}
