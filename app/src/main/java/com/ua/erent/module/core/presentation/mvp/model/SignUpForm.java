package com.ua.erent.module.core.presentation.mvp.model;

/**
 * Created by Максим on 10/29/2016.
 */

public final class SignUpForm {

    private String email;
    private String username;
    private String password;
    private String confPassword;

    public SignUpForm() {
    }

    public String getEmail() {
        return email;
    }

    public SignUpForm setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public SignUpForm setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public SignUpForm setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getConfPassword() {
        return confPassword;
    }

    public SignUpForm setConfPassword(String confPassword) {
        this.confPassword = confPassword;
        return this;
    }
}
