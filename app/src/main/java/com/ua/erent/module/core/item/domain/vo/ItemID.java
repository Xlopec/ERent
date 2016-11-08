package com.ua.erent.module.core.item.domain.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

/**
 * Created by Максим on 11/7/2016.
 */

public final class ItemID implements Parcelable {

    private final long id;

    public ItemID(long id) {

        if (id < 1)
            throw new IllegalArgumentException(
                    String.format(Locale.getDefault(), "id < 1, was %d", id));

        this.id = id;
    }

    private ItemID(Parcel in) {
        id = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemID> CREATOR = new Creator<ItemID>() {
        @Override
        public ItemID createFromParcel(Parcel in) {
            return new ItemID(in);
        }

        @Override
        public ItemID[] newArray(int size) {
            return new ItemID[size];
        }
    };

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemID itemID = (ItemID) o;

        return id == itemID.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "ItemID{" +
                "id=" + id +
                '}';
    }
}
