package com.ua.erent.module.core.account.auth.vo;

import android.os.Parcel;
import android.os.Parcelable;

import com.ua.erent.module.core.util.IBuilder;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 10/29/2016.
 */

public final class SignUpCredentials implements Parcelable {

    private final String email;
    private final String username;
    private final String password;
    private final String confPassword;

    public static final class Builder implements IBuilder<SignUpCredentials> {

        private String email;
        private String username;
        private String password;
        private String confPassword;

        public Builder() {
        }

        public String getEmail() {
            return email;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public String getUsername() {
            return username;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public String getConfPassword() {
            return confPassword;
        }

        public Builder setConfPassword(String confPassword) {
            this.confPassword = confPassword;
            return this;
        }

        @Override
        public SignUpCredentials build() {
            return new SignUpCredentials(this);
        }
    }

    private SignUpCredentials(Builder builder) {
        Preconditions.checkNotNull(builder);
        this.email = builder.getEmail();
        this.username = builder.getUsername();
        this.password = builder.getPassword();
        this.confPassword = builder.getPassword();
    }

    private SignUpCredentials(Parcel in) {
        email = in.readString();
        username = in.readString();
        password = in.readString();
        confPassword = in.readString();
    }

    public static final Creator<SignUpCredentials> CREATOR = new Creator<SignUpCredentials>() {
        @Override
        public SignUpCredentials createFromParcel(Parcel in) {
            return new SignUpCredentials(in);
        }

        @Override
        public SignUpCredentials[] newArray(int size) {
            return new SignUpCredentials[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(confPassword);
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfPassword() {
        return confPassword;
    }
}
