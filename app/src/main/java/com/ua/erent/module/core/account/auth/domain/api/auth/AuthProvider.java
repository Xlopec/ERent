package com.ua.erent.module.core.account.auth.domain.api.auth;

import android.accounts.Account;

import com.ua.erent.BuildConfig;
import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.account.auth.domain.vo.SignInCredentials;
import com.ua.erent.module.core.account.auth.domain.vo.SignUpCredentials;
import com.ua.erent.module.core.account.auth.user.domain.vo.UserID;
import com.ua.erent.module.core.app.Constant;
import com.ua.erent.module.core.exception.ApiException;
import com.ua.erent.module.core.exception.FileUploadException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Максим on 10/15/2016.
 */

public final class AuthProvider implements IAuthProvider {

    private static final String TAG = AuthProvider.class.getSimpleName();

    private final AuthApi api;

    /**
     * Represents server auth api
     */
    private interface AuthApi {
        /**
         * Fetches authorization token from API server
         *
         * @param requestBody authorization request json
         * @return rx observable to monitor request status
         */
        @POST("login")
        Observable<SignInResponse> fetchToken(@Body SignInRequest requestBody);

        /**
         * Sends sign up request to API server
         *
         * @param requestBody sign up request json
         * @return rx observable to monitor request status
         */
        @POST("register")
        Observable<SignUpResponse> signUp(@Body SignUpRequest requestBody);

        @Multipart
        @POST("profile/{userId}/avatar")
        Observable<Void> uploadAvatar(
                @Header("Authorization") String token,
                @Path("userId") long userId,
                @Part("avatar[file]") RequestBody avatar
        );

    }

    @Inject
    public AuthProvider(Retrofit retrofit) {
        api = retrofit.create(AuthApi.class);
    }

    @Override
    public Observable<Session> signIn(@NotNull SignInCredentials credentials) {

        final Observable<SignInResponse> call =
                api.fetchToken(new SignInRequest(credentials.getUsername(), credentials.getPassword(), BuildConfig.SERVER_API_KEY));

        return call.observeOn(AndroidSchedulers.mainThread())
                .map(authResponse -> {
                    final UserID userID = new UserID(authResponse.getUserId());
                    final Account account = new Account(credentials.getUsername(), Constant.ACCOUNT_TYPE);

                    return new Session(userID, account, authResponse.getToken());
                });
    }

    @Override
    public Observable<Session> signUp(@NotNull SignUpCredentials credentials) {

        final SignUpRequest.PlainPassword plainPassword =
                new SignUpRequest.PlainPassword(credentials.getPassword(), credentials.getConfPassword());

        final Observable<SignUpResponse> call =
                api.signUp(new SignUpRequest(credentials.getEmail(), credentials.getUsername(), plainPassword));

        return call.observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {

                    HttpException exception = (HttpException) throwable;

                    try {
                        String err = exception.response().errorBody().string();
                        System.out.println(err);

                        err = exception.response().body().toString();
                        System.out.println(err);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return Observable.error(
                        new ApiException(String.format("Failed to register user %s", credentials.getEmail())));
                }).map(signUpResponse -> {

                    final UserID userID = new UserID(signUpResponse.getUserId());
                    final Account account = new Account(credentials.getUsername(), Constant.ACCOUNT_TYPE);

                    return new Session(userID, account, signUpResponse.getToken());
                }).flatMap(session -> {

                    if (credentials.getAvatarImage() == null) {
                        return Observable.just(session);
                    }
                    final RequestBody avatar = RequestBody.create(MediaType.parse("image/*"), credentials.getAvatarImage());

                    return api.uploadAvatar(session.getToken(), session.getUserId().getId(), avatar)
                            .map(aVoid -> session)
                            .onErrorResumeNext(throwable ->
                                    Observable.error(new FileUploadException("Failed to upload avatar image")));
                });
    }

}
