package com.ua.erent.module.core.presentation.mvp.presenter.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Максим on 12/6/2016.
 */

public final class RegionModel implements Parcelable {

    private final long id;
    private final String name;

    public RegionModel(long id, String name) {
        this.id = id;
        this.name = name;
    }

    private RegionModel(Parcel in) {
        id = in.readLong();
        name = in.readString();
    }

    public static final Creator<RegionModel> CREATOR = new Creator<RegionModel>() {
        @Override
        public RegionModel createFromParcel(Parcel in) {
            return new RegionModel(in);
        }

        @Override
        public RegionModel[] newArray(int size) {
            return new RegionModel[size];
        }
    };

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegionModel that = (RegionModel) o;

        if (id != that.id) return false;
        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RegionModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
    }
}
