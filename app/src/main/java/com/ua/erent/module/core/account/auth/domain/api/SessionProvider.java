package com.ua.erent.module.core.account.auth.domain.api;

import android.util.Log;

import com.google.gson.Gson;
import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.domain.api.dto.AuthRequest;
import com.ua.erent.module.core.account.auth.dto.Credentials;
import com.ua.erent.module.core.app.Constant;
import com.ua.erent.module.core.networking.util.DefaultRetrieveRequest;
import com.ua.erent.module.core.networking.util.ErrorUtils;
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
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
        Observable<AuthResponse> authorize(@Body RequestBody requestBody);

    }

    @Inject
    public SessionProvider(Retrofit retrofit) {
        this.retrofit = retrofit;
        api = retrofit.create(AuthApi.class);
    }

    @Override
    public Observable<Session> fetchSession(@NotNull Credentials credentials) {

        final Observable<AuthResponse> call = api.authorize(RequestBody.create(
                MediaType.parse("application/json"),
                new Gson().toJson(new AuthRequest(credentials.getLogin(), credentials.getPassword(), ""))
        ));

        /*call.observeOn(AndroidSchedulers.mainThread()).
                map(authResponse ->
                        new Session(credentials.getLogin(), authResponse.getToken(), Constant.ACCOUNT_TOKEN_TYPE))
                .subscribe(new Subscriber<Session>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Session session) {

                    }
                });*/

       return call.observeOn(AndroidSchedulers.mainThread()).
                map(authResponse ->
                        new Session(credentials.getLogin(), authResponse.getToken(), Constant.ACCOUNT_TOKEN_TYPE));
    }

    public static <T> Subscription performAsyncRequest(Observable<T> observable, Action1<? super T> onAction, Action1<Throwable> onError) {
        // Specify a scheduler (Scheduler.newThread(), Scheduler.immediate(), ...)
        // We choose Scheduler.io() to perform network request in a thread pool
        return observable.subscribeOn(Schedulers.io())
                // Observe result in the main thread to be able to update UI
                .observeOn(AndroidSchedulers.mainThread())
                // Set callbacks actions
                .subscribe(onAction, onError);
    }

}
