package com.ua.erent.module.core.account.auth.domain.bo;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.ua.erent.module.core.account.auth.user.domain.vo.UserID;

import org.jetbrains.annotations.NotNull;

import dagger.internal.Preconditions;

/**
 * <p>
 * Business-object, describing app session
 * </p>
 * Created by Максим on 10/14/2016.
 */

public final class Session implements Parcelable {

    private final UserID userId;
    private final Account account;
    private final String token;

    public static final class Modifier {

        private UserID userId;
        private String username;
        private String accountType;
        private String token;

        public Modifier(@NotNull Session session) {
            Preconditions.checkNotNull(session);
            this.userId = session.userId;
            this.username = session.account.name;
            this.accountType = session.account.type;
            this.token = session.token;
        }

        public UserID getUserId() {
            return userId;
        }

        public Modifier setUserId(UserID userId) {
            this.userId = userId;
            return this;
        }

        public String getUsername() {
            return username;
        }

        public Modifier setUsername(String username) {
            this.username = username;
            return this;
        }

        public String getAccountType() {
            return accountType;
        }

        public Modifier setAccountType(String accountType) {
            this.accountType = accountType;
            return this;
        }

        public String getToken() {
            return token;
        }

        public Modifier setToken(String token) {
            this.token = token;
            return this;
        }

        public Session create() {
            return new Session(userId, new Account(username, accountType), token);
        }
    }

    public Session(UserID userId, Account account, String token) {
        Preconditions.checkNotNull(account, "account == null");
        Preconditions.checkNotNull(userId, "user id == null");

        this.account = account;
        this.userId = userId;
        this.token = token;
    }

    private Session(Parcel in) {
        token = in.readString();
        userId = in.readParcelable(UserID.class.getClassLoader());
        account = in.readParcelable(Account.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token);
        dest.writeParcelable(userId, flags);
        dest.writeParcelable(account, flags);
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
        return account.type;
    }

    public boolean isExpired() {
        return TextUtils.isEmpty(token);
    }

    public UserID getUserId() {
        return userId;
    }

    public String getUsername() {
        return account.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        if (!userId.equals(session.userId)) return false;
        if (!account.equals(session.account)) return false;
        return token != null ? token.equals(session.token) : session.token == null;

    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + account.hashCode();
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Session{" +
                "userId=" + userId +
                ", account=" + account +
                ", token='" + token + '\'' +
                '}';
    }
}
