package com.ua.erent.module.core.presentation.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import com.ua.erent.trash.AuthResponse;
import com.ua.erent.trash.ITestService;
import com.ua.erent.R;
import com.ua.erent.module.core.presentation.mvp.component.TestComponent;
import com.ua.erent.module.core.di.Injector;
import com.ua.erent.module.core.presentation.mvp.core.IBaseView;
import com.ua.erent.module.core.presentation.mvp.core.InjectableActivity;
import com.ua.erent.module.core.networking.component.NetworkingComponent;
import com.ua.erent.module.core.presentation.mvp.presenter.ITestPresenter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class MainActivity extends InjectableActivity<MainActivity, ITestPresenter> implements IBaseView {

    @Inject
    ITestService service;

    //@Inject
    //ITestPresenter presenter;

    @BindView(R.id.button)
    Button button;
    @BindView(R.id.text_view)
    TextView textView;
    @BindView(R.id.login_fld)
    EditText loginFld;
    @BindView(R.id.password_fld)
    EditText passwordFld;

    private static final String API_BASE = "http://erent.bget.ru/";

    private Retrofit retrofit;
    private IAuthAPI api;

    @NotNull
    @Override
    public Context getContext() {
        return this;
    }

    public interface IAuthAPI {

        @Headers("Content-Type: application/json")
        @POST("/app_acceptance.php/login")
        Call<AuthResponse> authorize(@Body RequestBody requestBody);

    }

    private static class AuthContainer {

        @SerializedName("username")
        String login;
        @SerializedName("password")
        String password;
        @SerializedName("apiKey")
        String apiKey;

    }

    public MainActivity() {
        super(R.layout.activity_main, TestComponent.class);
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final Buffer buffer = new Buffer();
            if (request != null)
                request.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkingComponent networkingComponent = Injector.injector().getComponent(NetworkingComponent.class);
        retrofit = networkingComponent.getRetrofit();
        api = retrofit.create(IAuthAPI.class);
        presenter.attachView(this, getIntent().getExtras(), savedInstanceState);
    }

    @OnClick(R.id.button)
    public void onClick(View view) {

        service.f();
        this.startActivity(new Intent(this, NextActivity.class));
        finish();
       /* AuthContainer authContainer = new AuthContainer();

        authContainer.login = loginFld.getText().toString();
        authContainer.password = passwordFld.getText().toString();
        authContainer.apiKey = "";

        Call<AuthResponse> call =
                api.authorize(RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(authContainer)));

        call.enqueue(new Callback<AuthResponse>() {

            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                String message = "Response code: " + response.code() + "\n";

                message += "Response body: ";

                AuthResponse authResponse = response.body();

                if (authResponse == null && response.errorBody() == null) {
                    message += "none";
                } else if (response.errorBody() != null) {
                    message += ErrorUtils.parseError(retrofit, response).toString();
                } else {
                    message += authResponse.toString();
                }

                textView.setText(message);
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                textView.setText("Failure: " + t.getMessage());
            }
        });*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
