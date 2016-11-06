package com.ua.erent.module.core.account.auth.user.api;

import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.account.auth.user.domain.vo.ContactInfo;
import com.ua.erent.module.core.account.auth.user.domain.vo.FullName;
import com.ua.erent.module.core.account.auth.user.domain.vo.PasswordForm;
import com.ua.erent.module.core.account.auth.user.domain.vo.UserForm;
import com.ua.erent.module.core.account.auth.user.domain.bo.User;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

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

        @PATCH("profile/{userId}")
        Observable<Void> updateUserProfile(
                @NotNull @Header("Authorization") String token,
                @Path("userId") long userId,
                @NotNull UserForm form
        );

        @POST("password/{userId}/change")
        Observable<Void> updatePassword(
                @NotNull @Header("Authorization") String token,
                @Path("userId") long userId,
                @NotNull ChangePasswordRequest request
        );

    }

    private final UserAPI api;

    @Inject
    public UserProvider(Retrofit retrofit) {
        this.api = retrofit.create(UserAPI.class);
    }

    @Override
    public Observable<User> fetchUserProfile(@NotNull Session session) {

        UserProvider.checkSession(session);

        return api.fetchUserProfile(session.getToken(), session.getUserId().getId())
                .map(response -> new User.Builder()
                        .setContactInfo(new ContactInfo(response.getEmail()))
                        .setFullName(new FullName(response.getUsername()))
                        .setId(session.getUserId())
                        .build()
                );
    }

    @Override
    public Observable<Void> updateUserProfile(@NotNull Session session, @NotNull UserForm userForm) {

        Preconditions.checkNotNull(userForm, "profile == null");
        UserProvider.checkSession(session);

        return api.updateUserProfile(session.getToken(), session.getUserId().getId(), userForm);
    }

    @Override
    public Observable<Void> changePassword(@NotNull Session session, @NotNull PasswordForm form) {

        UserProvider.checkSession(session);
        Preconditions.checkNotNull(form, "form == null");

        final ChangePasswordRequest.PlainPassword plainPassword =
                new ChangePasswordRequest.PlainPassword(form.getFirstPassword(), form.getSecondsPassword());
        final ChangePasswordRequest request = new ChangePasswordRequest(form.getCurrentPassword(), plainPassword);

        return api.updatePassword(session.getToken(), session.getUserId().getId(), request);
    }

    private static void checkSession(Session session) {

        Preconditions.checkNotNull(session, "session == null");

        if (session.isExpired())
            throw new IllegalStateException(String.format("%s session was expired", UserProvider.class.getSimpleName()));
    }

}
