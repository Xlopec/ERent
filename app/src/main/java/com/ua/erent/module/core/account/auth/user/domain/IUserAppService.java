package com.ua.erent.module.core.account.auth.user.domain;

import com.ua.erent.module.core.account.auth.vo.Profile;
import com.ua.erent.module.core.account.auth.vo.UserID;

import org.jetbrains.annotations.NotNull;

import rx.Observable;

/**
 * Created by Максим on 11/4/2016.
 */

public interface IUserAppService {

    Observable<User> fetchUserProfile(@NotNull UserID id);

    Observable<User> updateUserProfile(@NotNull Profile userProfile);

    User getCachedProfile();

    Observable<User> getOnUserProfileChangedObservable();

}
