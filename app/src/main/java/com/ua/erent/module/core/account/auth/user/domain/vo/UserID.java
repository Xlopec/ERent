package com.ua.erent.module.core.account.auth.user.domain.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

/**
 * <p>
 * Value object which represents user id in the app
 * </p>
 * Created by Максим on 11/4/2016.
 */

public final class UserID implements Parcelable {

    private final long id;

    public UserID(long id) {

        if (id < 1)
            throw new IllegalArgumentException(
                    String.format(Locale.getDefault(), "id < 1, was %d", id));

        this.id = id;
    }

    private UserID(Parcel in) {
        id = in.readLong();
    }

    public static final Creator<UserID> CREATOR = new Creator<UserID>() {
        @Override
        public UserID createFromParcel(Parcel in) {
            return new UserID(in);
        }

        @Override
        public UserID[] newArray(int size) {
            return new UserID[size];
        }
    };

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserID userID = (UserID) o;

        return id == userID.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "UserID{" +
                "id=" + id +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
    }
}
