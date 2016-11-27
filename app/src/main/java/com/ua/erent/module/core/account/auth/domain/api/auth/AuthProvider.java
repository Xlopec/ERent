package com.ua.erent.module.core.account.auth.domain.api.auth;

import android.accounts.Account;

import com.ua.erent.BuildConfig;
import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.account.auth.domain.vo.SignInCredentials;
import com.ua.erent.module.core.account.auth.domain.vo.SignUpCredentials;
import com.ua.erent.module.core.account.auth.user.domain.vo.UserID;
import com.ua.erent.module.core.app.Constant;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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
        @Headers("Content-Type: application/json")
        Observable<SignInResponse> fetchToken(@Body SignInRequest requestBody);

        /**
         * Sends sign up request to API server
         *
         * @param requestBody sign up request json
         * @return rx observable to monitor request status
         */
        @POST("register")
        @Headers("Content-Type: application/json")
        Observable<SignUpResponse> signUp(@Body SignUpRequest requestBody);

    }

    @Inject
    public AuthProvider(Retrofit retrofit) {
        this.api = retrofit.create(AuthApi.class);
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

        return api.signUp(new SignUpRequest(credentials.getEmail(), credentials.getUsername(), plainPassword))
                .observeOn(AndroidSchedulers.mainThread())
                .map(signUpResponse -> {

                    final UserID userID = new UserID(signUpResponse.getUserId());
                    final Account account = new Account(credentials.getUsername(), Constant.ACCOUNT_TYPE);

                    return new Session(userID, account, signUpResponse.getToken());
                });

    }
}
