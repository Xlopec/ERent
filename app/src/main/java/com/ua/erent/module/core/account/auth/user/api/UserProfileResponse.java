package com.ua.erent.module.core.account.auth.user.api;

import com.google.gson.annotations.SerializedName;

/**
 * <p>
 *     Represents server API response; see
 *     <a href="https://erent.online/api/doc#get--api-profile-{user}">link</a>
 * </p>
 * Created by Максим on 11/4/2016.
 */

final class UserProfileResponse {

    @SerializedName("id")
    private long id;
    @SerializedName("email")
    private String email;
    @SerializedName("username")
    private String username;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserProfileResponse{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
