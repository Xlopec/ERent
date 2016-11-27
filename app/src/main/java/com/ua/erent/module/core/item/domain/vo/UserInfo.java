package com.ua.erent.module.core.item.domain.vo;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.ua.erent.module.core.account.auth.user.domain.vo.UserID;
import com.ua.erent.module.core.util.validation.Regexes;

import org.apache.commons.validator.routines.RegexValidator;
import org.jetbrains.annotations.NotNull;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 11/26/2016.
 */

public final class UserInfo implements Parcelable {

    private static final RegexValidator USERNAME_VALIDATOR = new RegexValidator(Regexes.USERNAME);

    private final UserID owner;
    private final String username;
    private final Uri uri;

    public UserInfo(@NotNull UserID owner, @NotNull String username, @NotNull Uri uri) {

        if (!USERNAME_VALIDATOR.isValid(username))
            throw new IllegalArgumentException(String.format("invalid username %s", username));

        this.owner = Preconditions.checkNotNull(owner);
        this.username = username;
        this.uri = uri;
    }

    private UserInfo(Parcel in) {
        owner = in.readParcelable(UserID.class.getClassLoader());
        username = in.readString();
        uri = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(owner, flags);
        dest.writeString(username);
        dest.writeParcelable(uri, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public UserID getOwner() {
        return owner;
    }

    public String getUsername() {
        return username;
    }

    public Uri getUri() {
        return uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        if (!owner.equals(userInfo.owner)) return false;
        if (!username.equals(userInfo.username)) return false;
        return uri.equals(userInfo.uri);

    }

    @Override
    public int hashCode() {
        int result = owner.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + uri.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "owner=" + owner +
                ", username='" + username + '\'' +
                ", uri=" + uri +
                '}';
    }
}
