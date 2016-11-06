package com.ua.erent.module.core.account.auth.user.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Максим on 11/6/2016.
 */

final class ChangePasswordRequest {

    @SerializedName("current_password")
    private final String currentPassword;

    @SerializedName("plainPassword")
    private final PlainPassword plainPassword;

    static class PlainPassword {

        @SerializedName("first")
        private final String first;

        @SerializedName("second")
        private final String second;

        public PlainPassword(String first, String second) {
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

    public ChangePasswordRequest(String currentPassword, PlainPassword plainPassword) {
        this.currentPassword = currentPassword;
        this.plainPassword = plainPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public PlainPassword getPlainPassword() {
        return plainPassword;
    }
}
