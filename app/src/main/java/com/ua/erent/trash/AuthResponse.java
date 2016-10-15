package com.ua.erent.trash;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Максим on 10/9/2016.
 */
public final class AuthResponse {

    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "token='" + token + '\'' +
                '}';
    }
}
