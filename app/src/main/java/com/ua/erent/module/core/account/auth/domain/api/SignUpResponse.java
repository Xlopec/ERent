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

    @SerializedName("id")
    private final int userId;

    public SignUpResponse(String message, String token, int userId) {
        this.message = message;
        this.token = token;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "SignUpResponse{" +
                "message='" + message + '\'' +
                ", token='" + token + '\'' +
                ", userId=" + userId +
                '}';
    }
}
