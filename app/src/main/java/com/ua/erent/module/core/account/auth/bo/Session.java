package com.ua.erent.module.core.account.auth.bo;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.Locale;

/**
 * <p>
 *     Business-object, describing app session
 * </p>
 * Created by Максим on 10/14/2016.
 */

public final class Session implements Parcelable {

    private final String login;
    private final int userId;
    private final String token;
    private final String tokenType;

    public Session(String login, String token, String tokenType, int userId) {

        if(TextUtils.isEmpty(login))
            throw new IllegalArgumentException(String.format("illegal signIn, was %s", login));

        if(TextUtils.isEmpty(tokenType))
            throw new IllegalArgumentException(String.format("illegal token type, was %s", tokenType));

        if(TextUtils.isEmpty(token))
            throw new IllegalArgumentException(String.format("illegal token, was %s", token));

        if(userId < 1)
            throw new IllegalArgumentException(String.format(Locale.getDefault(), "illegal user id %d", userId));

        this.login = login;
        this.token = token;
        this.tokenType = tokenType;
        this.userId = userId;
    }

    private Session(Parcel in) {
        login = in.readString();
        token = in.readString();
        tokenType = in.readString();
        userId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(login);
        dest.writeString(token);
        dest.writeString(tokenType);
        dest.writeInt(userId);
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

    public String getLogin() {
        return login;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public boolean isExpired() {
        return TextUtils.isEmpty(getToken());
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        if (userId != session.userId) return false;
        if (!login.equals(session.login)) return false;
        if (!token.equals(session.token)) return false;
        return tokenType.equals(session.tokenType);

    }

    @Override
    public int hashCode() {
        int result = login.hashCode();
        result = 31 * result + userId;
        result = 31 * result + token.hashCode();
        result = 31 * result + tokenType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Session{" +
                "login='" + login + '\'' +
                ", userId=" + userId +
                ", token='" + token + '\'' +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }
}
