package com.ua.erent.module.core.account.auth.domain.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Максим on 10/29/2016.
 */

final class SignUpResponse {

    @SerializedName("msg")
    private final String message;

    @SerializedName("token")
    private final String token;

    public SignUpResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "SignUpResponse{" +
                "message='" + message + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
