package com.ua.erent.module.core.account.auth.domain.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Максим on 10/9/2016.
 */
final class SignInResponse {

    @SerializedName("token")
    private final String token;

    public SignInResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "SignInResponse{" +
                "token='" + token + '\'' +
                '}';
    }
}
