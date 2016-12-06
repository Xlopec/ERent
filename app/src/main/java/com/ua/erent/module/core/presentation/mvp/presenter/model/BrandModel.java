package com.ua.erent.module.core.presentation.mvp.presenter.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Максим on 12/6/2016.
 */

public final class BrandModel implements Parcelable {

    private final long id;
    private final String name;
    private final String description;

    public BrandModel(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    private BrandModel(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BrandModel> CREATOR = new Creator<BrandModel>() {
        @Override
        public BrandModel createFromParcel(Parcel in) {
            return new BrandModel(in);
        }

        @Override
        public BrandModel[] newArray(int size) {
            return new BrandModel[size];
        }
    };

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BrandModel that = (BrandModel) o;

        if (id != that.id) return false;
        if (!name.equals(that.name)) return false;
        return description != null ? description.equals(that.description) : that.description == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BrandModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
