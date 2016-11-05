package com.ua.erent.module.core.account.auth.vo;

import android.os.Parcel;
import android.os.Parcelable;

import com.ua.erent.module.core.util.IBuilder;
import com.ua.erent.module.core.util.validation.Regexes;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.RegexValidator;

import java.io.File;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 10/29/2016.
 */

public final class SignUpCredentials implements Parcelable {

    private final String email;
    private final String username;
    private final String password;
    private final String confPassword;
    private final File avatarImage;

    public static final class Builder implements IBuilder<SignUpCredentials> {

        private String email;
        private String username;
        private String password;
        private String confPassword;
        private File avatarImage;

        public Builder() {
        }

        public File getAvatarImage() {
            return avatarImage;
        }

        public Builder setAvatarImage(File avatarImage) {
            this.avatarImage = avatarImage;
            return this;
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

        final RegexValidator usernameValid = new RegexValidator(Regexes.SIGN_IN_USERNAME, true);
        final EmailValidator emailValidator = EmailValidator.getInstance();
        final RegexValidator passwordValidator = new RegexValidator(Regexes.PASSWORD, true);

        if (!usernameValid.isValid(builder.getUsername()))
            throw new IllegalArgumentException(String.format("Username %s is malformed", builder.getUsername()));

        if (!emailValidator.isValid(builder.getEmail()))
            throw new IllegalArgumentException(String.format("Email %s is malformed", builder.getEmail()));

        if (!passwordValidator.isValid(builder.getPassword()) ||
                !builder.getPassword().equals(builder.getConfPassword()))
            throw new IllegalArgumentException(String.format(
                    "Illegal password %s or conf pass %s", builder.getPassword(), builder.getConfPassword()));

        if (builder.getAvatarImage() != null && (!builder.getAvatarImage().exists() ||
                !builder.getAvatarImage().isFile()))
            throw new IllegalArgumentException(String.format("Invalid user avatar file exists %s, is file %s",
                    builder.getAvatarImage().exists(), builder.getAvatarImage().isFile()));

        this.email = builder.getEmail();
        this.username = builder.getUsername();
        this.password = builder.getPassword();
        this.confPassword = builder.getPassword();
        this.avatarImage = builder.getAvatarImage();
    }

    private SignUpCredentials(Parcel in) {
        email = in.readString();
        username = in.readString();
        password = in.readString();
        confPassword = in.readString();
        final String absPath = in.readString();
        avatarImage = absPath == null ? null : new File(absPath);
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
        if (avatarImage != null) {
            dest.writeString(avatarImage.getAbsolutePath());
        }
    }

    public File getAvatarImage() {
        return avatarImage;
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
