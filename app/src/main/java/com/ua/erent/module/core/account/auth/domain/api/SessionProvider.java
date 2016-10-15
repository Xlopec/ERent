package com.ua.erent.module.core.account.auth.domain.api;

import android.util.Log;

import com.google.gson.Gson;
import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.domain.api.dto.AuthRequest;
import com.ua.erent.module.core.account.auth.dto.Credentials;
import com.ua.erent.module.core.app.Constant;
import com.ua.erent.module.core.networking.util.DefaultRetrieveRequest;
import com.ua.erent.module.core.networking.util.ErrorUtils;
import com.ua.erent.module.core.networking.util.IRequest;
import com.ua.erent.module.core.util.IRetrieveCallback;
import com.ua.erent.trash.APIError;
import com.ua.erent.trash.AuthResponse;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Максим on 10/15/2016.
 */

public final class SessionProvider implements ISessionProvider {

    private static final String TAG = SessionProvider.class.getSimpleName();

    private final AuthApi api;
    private final Retrofit retrofit;

    private class RetrSessionReq extends DefaultRetrieveRequest<AuthResponse, Session> {

        private final Credentials credentials;

        RetrSessionReq(Credentials credentials, Call<AuthResponse> call, IRetrieveCallback<Session> callback) {
            super(call, callback);
            this.credentials = credentials;
        }

        @Override
        protected void doExecute() {

            mCall.enqueue(new Callback<AuthResponse>() {

                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                    if (response.errorBody() != null) {
                        final APIError error = ErrorUtils.parseError(retrofit, response);
                        callback.onException(new Exception(error.getMessage()));
                        return;
                    }

                    final AuthResponse authResponse = response.body();

                    if (authResponse == null || authResponse.getToken() == null) {
                        callback.onException(new Exception("token wasn't passed"));
                        return;
                    }

                    callback.onResult(new Session(credentials.getLogin(), authResponse.getToken(), Constant.ACCOUNT_TOKEN_TYPE));
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Log.e(TAG, "exception while retrieving session token", t);
                    callback.onException((Exception) t);
                }
            });
        }
    }

    private interface AuthApi {

        @Headers("Content-Type: application/json")
        @POST("/app_acceptance.php/login")
        Call<AuthResponse> authorize(@Body RequestBody requestBody);

    }

    @Inject
    public SessionProvider(Retrofit retrofit) {
        this.retrofit = retrofit;
        api = retrofit.create(AuthApi.class);
    }

    @Override
    public IRequest authAsync(@NotNull Credentials credentials, @NotNull IRetrieveCallback<Session> callback) {

        final Call<AuthResponse> call = api.authorize(RequestBody.create(
                MediaType.parse("application/json"),
                new Gson().toJson(new AuthRequest(credentials.getLogin(), credentials.getPassword(), ""))
        ));

        return new RetrSessionReq(credentials, call, callback);
    }
}
