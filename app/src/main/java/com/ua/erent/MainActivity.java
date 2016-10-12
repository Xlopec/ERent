package com.ua.erent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import com.ua.erent.module.core.di.Injector;
import com.ua.erent.module.core.networking.component.NetworkingComponent;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static com.ua.erent.module.core.di.Injector.injector;

public class MainActivity extends AppCompatActivity {

    @Inject
    ITestService service;

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

       // InjectionManager.instance().addMapping(getClass(), TestComponent.class);
        TestComponent provider = injector().getComponent(TestComponent.class);
        NetworkingComponent networkingComponent = Injector.injector().getComponent(NetworkingComponent.class);

        service = provider.getTestService();
        retrofit = networkingComponent.getRetrofit();
        api = retrofit.create(IAuthAPI.class);
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
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
