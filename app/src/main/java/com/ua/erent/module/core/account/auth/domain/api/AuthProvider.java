package com.ua.erent.module.core.account.auth.domain.api;

import com.ua.erent.BuildConfig;
import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.vo.SignInCredentials;
import com.ua.erent.module.core.account.auth.vo.SignUpCredentials;
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
        @POST("api/login")
        Observable<SignInResponse> fetchToken(@Body SignInRequest requestBody);

        /**
         * Sends sign up request to API server
         *
         * @param requestBody sign up request json
         * @return rx observable to monitor request status
         */
        @POST("api/register")
        Observable<SignUpResponse> signUp(@Body SignUpRequest requestBody);

        /**
         * Checks whether specified authorization token is valid
         *
         * @param requestBody authorization request json
         * @return rx observable to monitor request status
         */
        @Headers("Content-Type: application/json")
        @POST("api/login_check")
        Observable<SignInResponse> checkToken(@Body SignInRequest requestBody);

    }

    @Inject
    public AuthProvider(Retrofit retrofit) {
        api = retrofit.create(AuthApi.class);
    }

    @Override
    public Observable<Session> signIn(@NotNull SignInCredentials credentials) {

        final Observable<SignInResponse> call =
                api.fetchToken(new SignInRequest(credentials.getLogin(), credentials.getPassword(), BuildConfig.SERVER_API_KEY));

        return call.observeOn(AndroidSchedulers.mainThread())
                .map(authResponse -> new Session(credentials.getLogin(), authResponse.getToken(), Constant.ACCOUNT_TOKEN_TYPE));
    }

    @Override
    public Observable<Session> signUp(@NotNull SignUpCredentials credentials) {

        final SignUpRequest.PlainPassword plainPassword =
                new SignUpRequest.PlainPassword(credentials.getPassword(), credentials.getConfPassword());
        final Observable<SignUpResponse> call =
                api.signUp(new SignUpRequest(credentials.getEmail(), credentials.getUsername(), plainPassword));

        return call.observeOn(AndroidSchedulers.mainThread())
                .map(signUpResponse -> new Session(credentials.getUsername(), signUpResponse.getToken(), Constant.ACCOUNT_TOKEN_TYPE));
    }

}
