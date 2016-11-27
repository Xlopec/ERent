package com.ua.erent.module.core.account.auth.user.api;

import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.account.auth.user.domain.bo.User;
import com.ua.erent.module.core.account.auth.user.domain.vo.ContactInfo;
import com.ua.erent.module.core.account.auth.user.domain.vo.FullName;
import com.ua.erent.module.core.account.auth.user.domain.vo.PasswordForm;
import com.ua.erent.module.core.account.auth.user.domain.vo.UserForm;
import com.ua.erent.module.core.exception.FileUploadException;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
        @Headers("Content-Type: application/json")
        Observable<UserProfileResponse> fetchUserProfile(
                @NotNull @Header("Authorization") String token,
                @Path("userId") long userId
        );

        @PATCH("profile/{userId}")
        @Headers("Content-Type: application/json")
        Observable<Void> updateUserProfile(
                @NotNull @Header("Authorization") String token,
                @Path("userId") long userId,
                @NotNull UserForm form
        );

        @POST("password/{userId}/change")
        @Headers("Content-Type: application/json")
        Observable<Void> updatePassword(
                @NotNull @Header("Authorization") String token,
                @Path("userId") long userId,
                @NotNull ChangePasswordRequest request
        );

        @Multipart
        @POST("profile/{userId}/avatar")
        Observable<Void> uploadAvatar(
                @Header("Authorization") String token,
                @Path("userId") long userId,
                @Part("avatar[file]\"; filename=\"avatar") RequestBody avatar
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

    @Override
    public Observable<Void> uploadAvatar(@NotNull Session session, @NotNull File avatar) {

        UserProvider.checkSession(session);
        final String mimeType = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(avatar).toString());
        final RequestBody body = RequestBody.create(MediaType.parse("image/".concat(mimeType)), avatar);

        return api.uploadAvatar(session.getToken(), session.getUserId().getId(), body)
                .onErrorResumeNext(throwable -> Observable.error(new FileUploadException("Failed to upload avatar image")));
    }

    private static void checkSession(Session session) {

        Preconditions.checkNotNull(session, "session == null");

        if (session.isExpired())
            throw new IllegalStateException(String.format("%s session was expired", UserProvider.class.getSimpleName()));
    }

}
