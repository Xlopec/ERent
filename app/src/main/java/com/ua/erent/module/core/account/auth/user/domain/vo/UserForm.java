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
    private final FormField<String> phone;
    private final FormField<String> skype;

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
        private FormField<String> phone;
        private FormField<String> skype;

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

        public final Builder setPhone(String phone) {
            this.phone = new FormField<>(phone);
            return this;
        }

        public final Builder setSkype(String skype) {
            this.skype = new FormField<>(skype);
            return this;
        }

        protected final FormField<String> getEmailField() {
            return username;
        }

        protected final FormField<String> getUsernameField() {
            return username;
        }

        protected final FormField<String> getPhoneField() {
            return phone;
        }

        protected final FormField<String> getSkypeField() {
            return skype;
        }

        public void reset() {
            username = new FormField<>();
            email = new FormField<>();
            skype = new FormField<>();
            phone = new FormField<>();
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
        this.phone = builder.getPhoneField();
        this.skype = builder.getSkypeField();
    }

    private UserForm(Parcel in) {
        username = in.readByte() == 1 ? new FormField<>() : new FormField<>(in.readString());
        email = in.readByte() == 1 ? new FormField<>() : new FormField<>(in.readString());
        phone = in.readByte() == 1 ? new FormField<>() : new FormField<>(in.readString());
        skype = in.readByte() == 1 ? new FormField<>() : new FormField<>(in.readString());
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

    public String getPhone() {
        return phone.getValue();
    }

    public String getSkype() {
        return skype.getValue();
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
        dest.writeByte((byte) (phone.isEmpty() ? 1 : 0));
        dest.writeString(phone.getValue());
        dest.writeByte((byte) (skype.isEmpty() ? 1 : 0));
        dest.writeString(skype.getValue());
    }

    @Override
    public String toString() {
        return "UserForm{" +
                "username=" + username +
                ", email=" + email +
                ", phone=" + phone +
                ", skype=" + skype +
                '}';
    }
}
