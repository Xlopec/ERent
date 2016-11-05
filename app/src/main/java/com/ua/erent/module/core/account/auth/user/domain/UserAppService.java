package com.ua.erent.module.core.account.auth.user.domain;

import com.ua.erent.module.core.account.auth.vo.Profile;
import com.ua.erent.module.core.account.auth.vo.UserID;

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
    public Observable<User> fetchUserProfile(@NotNull UserID id) {
        return domain.fetchUserProfile(id);
    }

    @Override
    public Observable<User> updateUserProfile(@NotNull Profile userProfile) {
        return domain.updateUserProfile(userProfile);
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
