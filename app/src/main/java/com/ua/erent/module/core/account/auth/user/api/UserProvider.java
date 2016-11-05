package com.ua.erent.module.core.account.auth.user.api;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.user.domain.User;
import com.ua.erent.module.core.account.auth.vo.ContactInfo;
import com.ua.erent.module.core.account.auth.vo.FullName;
import com.ua.erent.module.core.account.auth.vo.UserID;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Максим on 11/4/2016.
 */

public final class UserProvider implements IUserProvider {

    /**
     * Represents RESTfull server profile api
     */
    private interface UserAPI {

        @GET("profile/{userId}")
        Observable<UserProfileResponse> fetchUserProfile(
                @NotNull @Header("Authorization") String token,
                @Path("userId") long userId
        );
    }

    private final UserAPI api;

    @Inject
    public UserProvider(Retrofit retrofit) {
        this.api = retrofit.create(UserAPI.class);
    }

    @Override
    public Observable<User> fetchUserProfile(@NotNull Session session, @NotNull UserID id) {

        Preconditions.checkNotNull(session, "session == null");
        Preconditions.checkNotNull(id, "id == null");

        if (session.isExpired())
            throw new IllegalStateException(String.format("%s session was expired", getClass().getSimpleName()));

        return api.fetchUserProfile(session.getToken(), id.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> new User.Builder()
                        .setContactInfo(new ContactInfo(response.getEmail()))
                        .setFullName(new FullName(response.getUsername()))
                        .setId(id)
                        .build()
                );
    }
}
