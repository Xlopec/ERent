package com.ua.erent.module.core.account.auth.domain.api;

import com.google.gson.annotations.SerializedName;

/**
 * <p>
 * Represents sign up api request body
 * </p>
 * Created by Максим on 10/29/2016.
 */

final class SignUpRequest {

    @SerializedName("email")
    private final String email;
    @SerializedName("username")
    private final String username;
    @SerializedName("plainPassword")
    private final PlainPassword plainPassword;

    static final class PlainPassword {

        @SerializedName("first")
        private final String first;
        @SerializedName("second")
        private final String second;

        PlainPassword(String first, String second) {
            this.first = first;
            this.second = second;
        }

        public String getFirst() {
            return first;
        }

        public String getSecond() {
            return second;
        }
    }

    public SignUpRequest(String email, String username, PlainPassword plainPassword) {
        this.email = email;
        this.username = username;
        this.plainPassword = plainPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public PlainPassword getPlainPassword() {
        return plainPassword;
    }

    @Override
    public String toString() {
        return "SignUpRequest{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", plainPassword=" + plainPassword +
                '}';
    }
}
