package com.ua.erent.module.core.account.auth.domain.api.auth;

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
        Observable<SignInResponse> fetchToken(@Body SignInRequest requestBody);

        /**
         * Sends sign up request to API server
         *
         * @param requestBody sign up request json
         * @return rx observable to monitor request status
         */
        @POST("register")
        Observable<SignUpResponse> signUp(@Body SignUpRequest requestBody);

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
                .map(authResponse -> new Session(new UserID(authResponse.getUserId()), authResponse.getToken(),
                        credentials.getUsername(), Constant.ACCOUNT_TOKEN_TYPE));
    }

    @Override
    public Observable<Session> signUp(@NotNull SignUpCredentials credentials) {

        final SignUpRequest.PlainPassword plainPassword =
                new SignUpRequest.PlainPassword(credentials.getPassword(), credentials.getConfPassword());
        final Observable<SignUpResponse> call =
                api.signUp(new SignUpRequest(credentials.getEmail(), credentials.getUsername(), plainPassword));

        return call.observeOn(AndroidSchedulers.mainThread())
                .map(signUpResponse -> new Session(new UserID(signUpResponse.getUserId()), signUpResponse.getToken(),
                        credentials.getUsername(), Constant.ACCOUNT_TOKEN_TYPE));
    }

}