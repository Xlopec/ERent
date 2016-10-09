package com.ua.erent;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Максим on 10/9/2016.
 */
public class AuthResponse {

    @SerializedName("token")
    private String token;

    @Override
    public String toString() {
        return "AuthResponse{" +
                "token='" + token + '\'' +
                '}';
    }
}
