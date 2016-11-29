package com.ua.erent.module.core.item.domain.vo;

import android.os.Parcel;
import android.os.Parcelable;

import com.ua.erent.module.core.account.auth.user.domain.vo.UserID;
import com.ua.erent.module.core.presentation.mvp.view.util.MyURL;
import com.ua.erent.module.core.util.validation.Regexes;

import org.apache.commons.validator.routines.RegexValidator;
import org.jetbrains.annotations.NotNull;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 11/26/2016.
 */

public final class UserInfo implements Parcelable {

    private static final RegexValidator USERNAME_VALIDATOR = new RegexValidator(Regexes.USERNAME);

    private final UserID id;
    private final String username;
    private final MyURL avatar;

    public UserInfo(@NotNull UserID id, @NotNull String username, @NotNull MyURL avatar) {

        if (!USERNAME_VALIDATOR.isValid(username))
            throw new IllegalArgumentException(String.format("invalid username %s", username));

        this.id = Preconditions.checkNotNull(id);
        this.username = username;
        this.avatar = avatar;
    }

    private UserInfo(Parcel in) {
        id = in.readParcelable(UserID.class.getClassLoader());
        username = in.readString();
        avatar = in.readParcelable(MyURL.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(id, flags);
        dest.writeString(username);
        dest.writeParcelable(avatar, flags);
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

    public UserID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public MyURL getAvatar() {
        return avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        if (!id.equals(userInfo.id)) return false;
        if (!username.equals(userInfo.username)) return false;
        return avatar.equals(userInfo.avatar);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + avatar.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", avatar=" + avatar +
                '}';
    }
}
