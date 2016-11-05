package com.ua.erent.module.core.networking.util;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import dagger.internal.Preconditions;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * <p>
 * This class contains general set of api server media types
 * </p>
 * Created by Максим on 10/22/2016.
 */

public final class ApiUtils {

    private ApiUtils() {
        throw new RuntimeException();
    }

    public static final MediaType APP_JSON = MediaType.parse("application/json");

    public static <T> String toJson(@NotNull T t) {
        return new Gson().toJson(Preconditions.checkNotNull(t));
    }

    public static <T> RequestBody createReqBody(@NotNull MediaType media, @NotNull T t) {
        return RequestBody.create(media, ApiUtils.toJson(toJson(t)));
    }

    public static <T> RequestBody createReqBody(@NotNull T t) {
        return createReqBody(APP_JSON, t);
    }

    public static String toApiToken(@NotNull String token) {
        return String.format("Bearer %s", Preconditions.checkNotNull(token));
    }

}
