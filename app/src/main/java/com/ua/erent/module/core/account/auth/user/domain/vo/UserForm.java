package com.ua.erent.module.core.account.auth.user.domain.vo;

import android.os.Parcel;
import android.os.Parcelable;

import com.ua.erent.module.core.util.FormField;
import com.ua.erent.module.core.util.IBuilder;

import org.jetbrains.annotations.NotNull;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 11/5/2016.
 */

public final class UserForm implements Parcelable {

    private final FormField<String> username;
    private final FormField<String> email;

    public static final Creator<UserForm> CREATOR = new Creator<UserForm>() {
        @Override
        public UserForm createFromParcel(Parcel in) {
            return new UserForm(in);
        }

        @Override
        public UserForm[] newArray(int size) {
            return new UserForm[size];
        }
    };

    public static class Builder implements IBuilder<UserForm> {

        private FormField<String> username;
        private FormField<String> email;

        public Builder() {
            reset();
        }

        public final String getUsername() {
            return username.getValue();
        }

        public final Builder setUsername(String username) {
            this.username = new FormField<>(username);
            return this;
        }

        public final String getEmail() {
            return email.getValue();
        }

        public final Builder setEmail(String email) {
            this.email = new FormField<>(email);
            return this;
        }

        protected final FormField<String> getEmailField() {
            return username;
        }

        protected final FormField<String> getUsernameField() {
            return username;
        }

        public void reset() {
            username = new FormField<>();
            email = new FormField<>();
        }

        @Override
        public UserForm build() {
            return new UserForm(this);
        }
    }

    private UserForm(@NotNull Builder builder) {
        Preconditions.checkNotNull(builder);
        this.username = Preconditions.checkNotNull(builder.getUsernameField(), "field wasn't set");
        this.email = Preconditions.checkNotNull(builder.getEmailField());
    }

    private UserForm(Parcel in) {
        username = in.readByte() == 1 ? new FormField<>() : new FormField<>(in.readString());
        email = in.readByte() == 1 ? new FormField<>() : new FormField<>(in.readString());
    }

    public FormField<String> getUsernameField() {
        return username;
    }

    public FormField<String> getEmailField() {
        return email;
    }

    public String getUsername() {
        return username.getValue();
    }

    public String getEmail() {
        return email.getValue();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (username.isEmpty() ? 1 : 0));
        dest.writeString(username.getValue());
        dest.writeByte((byte) (email.isEmpty() ? 1 : 0));
        dest.writeString(email.getValue());
    }

    @Override
    public String toString() {
        return "Profile{" +
                "username=" + username +
                ", email=" + email +
                '}';
    }
}
