package com.ua.erent.module.core.account.auth.user.domain;

import com.ua.erent.module.core.account.auth.user.domain.vo.PasswordForm;
import com.ua.erent.module.core.account.auth.user.domain.vo.UserForm;
import com.ua.erent.module.core.account.auth.user.domain.bo.User;

import org.jetbrains.annotations.NotNull;

import rx.Observable;

/**
 * Created by Максим on 11/4/2016.
 */

public interface IUserAppService {

    Observable<User> fetchUserProfile();

    Observable<User> updateUserProfile(@NotNull UserForm userUserForm);

    Observable<Void> changePassword(@NotNull PasswordForm form);

    User getCachedProfile();

    Observable<User> getOnUserProfileChangedObservable();

}
