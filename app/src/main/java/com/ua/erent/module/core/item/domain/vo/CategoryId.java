package com.ua.erent.module.core.item.domain.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Максим on 11/12/2016.
 */

public class CategoryID implements Parcelable {

    private final int id;

    public CategoryID(int id) {

        if (id < 1)
            throw new IllegalArgumentException(String.format("id < 1 %s", id));

        this.id = id;
    }

    private CategoryID(Parcel in) {
        id = in.readInt();
    }

    public static final Creator<CategoryID> CREATOR = new Creator<CategoryID>() {
        @Override
        public CategoryID createFromParcel(Parcel in) {
            return new CategoryID(in);
        }

        @Override
        public CategoryID[] newArray(int size) {
            return new CategoryID[size];
        }
    };

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "CategoryID{" +
                "id=" + id +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryID that = (CategoryID) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
