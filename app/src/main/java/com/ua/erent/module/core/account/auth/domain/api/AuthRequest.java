package com.ua.erent.module.core.account.auth.domain.api;

import com.google.gson.annotations.SerializedName;

/**
 * <p>
 *     Represents auth api request body
 * </p>
 * Created by Максим on 10/15/2016.
 */

final class AuthRequest {

    @SerializedName("username")
    private final String login;

    @SerializedName("password")
    private final String password;

    @SerializedName("apiKey")
    private final String apiKey;

    AuthRequest(String login, String password, String apiKey) {
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

}
