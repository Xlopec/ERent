package com.ua.erent.module.core.account.auth.user.api;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.user.domain.User;
import com.ua.erent.module.core.account.auth.vo.UserID;

import org.jetbrains.annotations.NotNull;

import rx.Observable;

/**
 * <p>
 *     Represents provider which interacts with server API
 * </p>
 * Created by Максим on 11/4/2016.
 */

public interface IUserProvider {

    Observable<User> fetchUserProfile(@NotNull Session session, @NotNull UserID id);

}
