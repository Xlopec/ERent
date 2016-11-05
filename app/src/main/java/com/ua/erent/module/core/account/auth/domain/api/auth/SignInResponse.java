package com.ua.erent.module.core.account.auth.domain.api.auth;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Максим on 10/9/2016.
 */
final class SignInResponse {

    @SerializedName("token")
    private final String token;

    @SerializedName("id")
    private final int userId;

    public SignInResponse(String token, int userId) {
        this.token = token;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "SignInResponse{" +
                "token='" + token + '\'' +
                ", userId=" + userId +
                '}';
    }
}
