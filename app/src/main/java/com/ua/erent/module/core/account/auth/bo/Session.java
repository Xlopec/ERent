package com.ua.erent.module.core.account.auth.bo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>
 *     Business-object, describing app session
 * </p>
 * Created by Максим on 10/14/2016.
 */

public final class Session implements Parcelable {

    private final String login;
    private final String token;
    private final String tokenType;

    public Session(String login, String token, String tokenType) {
        this.login = login;
        this.token = token;
        this.tokenType = tokenType;
    }

    private Session(Parcel in) {
        login = in.readString();
        token = in.readString();
        tokenType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(login);
        dest.writeString(token);
        dest.writeString(tokenType);
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
        return token == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        if (!login.equals(session.login)) return false;
        if (token != null ? !token.equals(session.token) : session.token != null) return false;
        return tokenType.equals(session.tokenType);

    }

    @Override
    public int hashCode() {
        int result = login.hashCode();
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + tokenType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Session{" +
                "login='" + login + '\'' +
                ", token='" + token + '\'' +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }
}
