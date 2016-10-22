package com.ua.erent.module.core.account.auth.domain.api;

import com.ua.erent.BuildConfig;
import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.vo.Credentials;
import com.ua.erent.module.core.app.Constant;
import com.ua.erent.module.core.networking.util.ApiUtils;
import com.ua.erent.trash.AuthResponse;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Максим on 10/15/2016.
 */

public final class SessionProvider implements ISessionProvider {

    private static final String TAG = SessionProvider.class.getSimpleName();

    private final AuthApi api;

    /**
     * Represents server auth api
     */
    private interface AuthApi {

        @Headers("Content-Type: application/json")
        @POST("/app_acceptance.php/login")
        Observable<AuthResponse> authorize(@Body RequestBody requestBody);

    }

    @Inject
    public SessionProvider(Retrofit retrofit) {
        api = retrofit.create(AuthApi.class);
    }

    @Override
    public Observable<Session> fetchSession(@NotNull Credentials credentials) {

        final Observable<AuthResponse> call =
                api.authorize(ApiUtils.createReqBody(new AuthRequest(credentials.getLogin(), credentials.getPassword(), BuildConfig.SERVER_API_KEY)));

        return call.observeOn(AndroidSchedulers.mainThread()).
                map(authResponse -> new Session(credentials.getLogin(), authResponse.getToken(), Constant.ACCOUNT_TOKEN_TYPE));
    }

}
