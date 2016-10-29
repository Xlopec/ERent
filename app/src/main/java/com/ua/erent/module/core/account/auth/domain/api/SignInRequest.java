package com.ua.erent.module.core.account.auth.domain.api;

import com.google.gson.annotations.SerializedName;

/**
 * <p>
 *     Represents signIn api request body
 * </p>
 * Created by Максим on 10/15/2016.
 */

final class SignInRequest {

    @SerializedName("username")
    private final String login;

    @SerializedName("password")
    private final String password;

    @SerializedName("apiKey")
    private final String apiKey;

    SignInRequest(String login, String password, String apiKey) {
        this.login = login;
        this.password = password;
        this.apiKey = apiKey;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getApiKey() {
        return apiKey;
    }

    @Override
    public String toString() {
        return "SignInRequest{" +
                "signIn='" + login + '\'' +
                ", password='" + password + '\'' +
                ", apiKey='" + apiKey + '\'' +
                '}';
    }
}
