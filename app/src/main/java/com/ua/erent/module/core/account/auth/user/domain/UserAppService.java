package com.ua.erent.module.core.account.auth.user.domain;

import com.ua.erent.module.core.account.auth.user.domain.vo.PasswordForm;
import com.ua.erent.module.core.account.auth.user.domain.vo.UserForm;
import com.ua.erent.module.core.account.auth.user.domain.bo.User;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Максим on 11/5/2016.
 */

public final class UserAppService implements IUserAppService {

    private final IUserDomain domain;

    @Inject
    public UserAppService(IUserDomain domain) {
        this.domain = domain;
    }

    @Override
    public Observable<User> fetchUserProfile() {
        return domain.fetchUserProfile();
    }

    @Override
    public Observable<User> updateUserProfile(@NotNull UserForm userUserForm) {
        return domain.updateUserProfile(userUserForm);
    }

    @Override
    public Observable<Void> changePassword(@NotNull PasswordForm form) {
        return domain.changePassword(form);
    }

    @Override
    public User getCachedProfile() {
        return domain.getCachedProfile();
    }

    @Override
    public Observable<User> getOnUserProfileChangedObservable() {
        return domain.getOnUserProfileChangedObservable();
    }
}
