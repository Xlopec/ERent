package com.ua.erent.module.core.networking.config;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ua.erent.BuildConfig;
import com.ua.erent.module.core.config.IConfigModule;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <p>
 *     Contains set of Retrofit library configurations
 * </p>
 * Created by Максим on 10/12/2016.
 */
public final class RetrofitConfigModule extends IConfigModule < Retrofit > {

    private static final String API_BASE = "http://erent.bget.ru/";

    private Retrofit retrofit;

    @Override
    public Retrofit configure(@NotNull Application application) {

        if(retrofit != null) return retrofit;

        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        final Gson gson = new GsonBuilder().create();
        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // http request/response logging
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE);
        httpClient.
                addNetworkInterceptor(
                        (chain) -> {
                            // packet interception
                            final Request original = chain.request();
                            /*final String toString = bodyToString(original.body());

                            Log.d("Tag", "Request body: " + toString);*/

                            final Request request = original.newBuilder().
                                    method(original.method(), original.body()).
                                    build();

                            return chain.proceed(request);
                        }
                ).addInterceptor(logging);

        return retrofit = new Retrofit.Builder().
                baseUrl(API_BASE).
                client(httpClient.build()).
                addConverterFactory(GsonConverterFactory.create(gson)).build();
    }

}
