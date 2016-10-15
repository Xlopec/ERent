package com.ua.erent.module.core.networking.util;

import com.ua.erent.trash.APIError;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Максим on 10/9/2016.
 */
public final class ErrorUtils {

    private ErrorUtils() {
        throw new RuntimeException();
    }

    public static APIError parseError(Retrofit retrofit, Response<?> response) {

        final Converter<ResponseBody, APIError> converter =
                retrofit.responseBodyConverter(APIError.class, new Annotation[0]);

        APIError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new APIError();
        }

        return error;
    }
}
