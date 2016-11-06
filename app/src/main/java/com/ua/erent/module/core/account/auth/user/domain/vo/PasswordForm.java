package com.ua.erent.module.core.account.auth.user.domain.vo;

import android.os.Parcel;
import android.os.Parcelable;

import com.ua.erent.module.core.util.IBuilder;
import com.ua.erent.module.core.util.validation.Regexes;

import org.apache.commons.validator.routines.RegexValidator;
import org.jetbrains.annotations.NotNull;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 11/6/2016.
 */

public final class PasswordForm implements Parcelable {

    private final String currentPassword;
    private final String firstPassword;
    private final String secondsPassword;

    public static final Creator<PasswordForm> CREATOR = new Creator<PasswordForm>() {
        @Override
        public PasswordForm createFromParcel(Parcel in) {
            return new PasswordForm(in);
        }

        @Override
        public PasswordForm[] newArray(int size) {
            return new PasswordForm[size];
        }
    };

    public static final class Builder implements IBuilder<PasswordForm> {

        private String currentPassword;
        private String firstPassword;
        private String secondsPassword;

        public Builder() {
        }

        public String getCurrentPassword() {
            return currentPassword;
        }

        public Builder setCurrentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
            return this;
        }

        public String getFirstPassword() {
            return firstPassword;
        }

        public Builder setFirstPassword(String firstPassword) {
            this.firstPassword = firstPassword;
            return this;
        }

        public String getSecondsPassword() {
            return secondsPassword;
        }

        public Builder setSecondsPassword(String secondsPassword) {
            this.secondsPassword = secondsPassword;
            return this;
        }

        @Override
        public PasswordForm build() {
            return new PasswordForm(this);
        }
    }

    private PasswordForm(@NotNull Builder builder) {
        Preconditions.checkNotNull(builder);

        final RegexValidator passwordValidator = new RegexValidator(Regexes.PASSWORD, true);

        if (!passwordValidator.isValid(builder.getCurrentPassword()))
            throw new IllegalArgumentException(
                    String.format("illegal current password %s", builder.getCurrentPassword()));

        if (!passwordValidator.isValid(builder.getFirstPassword()) ||
                !builder.getFirstPassword().equals(builder.getSecondsPassword()))
            throw new IllegalArgumentException(String.format(
                    "Illegal password %s or conf pass %s", builder.getFirstPassword(),
                    builder.getSecondsPassword()));

        this.currentPassword = builder.getCurrentPassword();
        this.firstPassword = builder.getFirstPassword();
        this.secondsPassword = builder.getSecondsPassword();
    }

    private PasswordForm(Parcel in) {
        currentPassword = in.readString();
        firstPassword = in.readString();
        secondsPassword = in.readString();
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getFirstPassword() {
        return firstPassword;
    }

    public String getSecondsPassword() {
        return secondsPassword;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(currentPassword);
        dest.writeString(firstPassword);
        dest.writeString(secondsPassword);
    }

    @Override
    public String toString() {
        return "PasswordForm{" +
                "currentPassword='" + currentPassword + '\'' +
                ", firstPassword='" + firstPassword + '\'' +
                ", secondsPassword='" + secondsPassword + '\'' +
                '}';
    }
}
