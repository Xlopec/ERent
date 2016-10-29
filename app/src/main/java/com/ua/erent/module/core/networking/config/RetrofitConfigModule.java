package com.ua.erent.module.core.networking.config;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ua.erent.BuildConfig;
import com.ua.erent.module.core.config.IConfigModule;
import com.ua.erent.module.core.networking.service.IPacketInterceptService;
import com.ua.erent.module.core.util.IBuilder;

import java.io.IOException;

import dagger.internal.Preconditions;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * <p>
 * Contains set of Retrofit library configurations
 * </p>
 * Created by Максим on 10/12/2016.
 */
public final class RetrofitConfigModule extends IConfigModule<Retrofit> {

    private final IPacketInterceptService interceptService;

    public static final class Builder implements IBuilder<RetrofitConfigModule> {

        private IPacketInterceptService interceptService;

        public IPacketInterceptService getInterceptService() {
            return interceptService;
        }

        public Builder setInterceptService(IPacketInterceptService interceptService) {
            this.interceptService = interceptService;
            return this;
        }

        @Override
        public RetrofitConfigModule build() {
            return new RetrofitConfigModule(this);
        }
    }

    private Retrofit retrofit;

    private RetrofitConfigModule(Builder builder) {
        Preconditions.checkNotNull(builder);
        this.interceptService = builder.getInterceptService();
    }

    @Override
    public Retrofit configure() {

        if (retrofit != null) return retrofit;

        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        final Gson gson = new GsonBuilder().create();
        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // http request/response logging
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE);

        if (interceptService != null) {

            httpClient.addInterceptor(chain -> {
                final Response response = chain.proceed(chain.request());
                interceptService.intercept(response);
                return response;
            });
        }

        httpClient.addNetworkInterceptor(
                (chain) -> {
                    // packet interception
                    final Request original = chain.request();
                            final String toString = bodyToString(original.body());

                            Log.d("Tag", "Request body: " + toString);

                    final Request request = original.newBuilder().
                            header("Content-Type", "application/json").
                            method(original.method(), original.body()).
                            build();

                    return chain.proceed(request);
                }
        ).addInterceptor(logging);

        return retrofit = new Retrofit.Builder().
                baseUrl(BuildConfig.API_BASE_URL).
                client(httpClient.build()).
                addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io())).
                addConverterFactory(GsonConverterFactory.create(gson)).build();
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
            return "did not work";
        }
    }

}
