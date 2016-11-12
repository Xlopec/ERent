package com.ua.erent.module.core.account.auth.domain.bo;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.ua.erent.module.core.account.auth.user.domain.vo.UserID;

import dagger.internal.Preconditions;

/**
 * <p>
 * Business-object, describing app session
 * </p>
 * Created by Максим on 10/14/2016.
 */

public final class Session implements Parcelable {

    private final UserID userId;
    private final String username;
    private final String token;
    private final String tokenType;

    public Session(UserID userId, String token, String username, String tokenType) {

        if(TextUtils.isEmpty(username))
            throw new IllegalArgumentException(String.format("illegal username, was %s", username));

        if (TextUtils.isEmpty(tokenType))
            throw new IllegalArgumentException(String.format("illegal token type, was %s", tokenType));

        this.token = token;
        this.tokenType = tokenType;
        this.userId = Preconditions.checkNotNull(userId);
        this.username = username;
    }

    private Session(Parcel in) {
        token = in.readString();
        tokenType = in.readString();
        userId = in.readParcelable(UserID.class.getClassLoader());
        username = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token);
        dest.writeString(tokenType);
        dest.writeParcelable(userId, flags);
        dest.writeString(username);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Session> CREATOR = new Creator<Session>() {
        @Override
        public Session createFromParcel(Parcel in) {
            return new Session(in);
        }

        @Override
        public Session[] newArray(int size) {
            return new Session[size];
        }
    };

    public String getRawToken() {
        return token;
    }

    public String getToken() {
        return String.format("Bearer %s", getRawToken());
    }

    public String getTokenType() {
        return tokenType;
    }

    public boolean isExpired() {
        return TextUtils.isEmpty(token);
    }

    public UserID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        if (!userId.equals(session.userId)) return false;
        if (!username.equals(session.username)) return false;
        if (!token.equals(session.token)) return false;
        return tokenType.equals(session.tokenType);

    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + token.hashCode();
        result = 31 * result + tokenType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Session{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }
}
