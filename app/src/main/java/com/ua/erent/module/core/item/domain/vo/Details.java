package com.ua.erent.module.core.item.domain.vo;

import android.os.Parcel;
import android.os.Parcelable;

import com.ua.erent.module.core.util.IBuilder;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 11/7/2016.
 */

public final class Details implements Parcelable {

    private final String username;
    private final Brand brand;
    private final Region region;
    private final DateTime publicationDate;

    public final static class Builder implements IBuilder<Details> {

        private String username;
        private Brand brand;
        private DateTime publicationDate;
        private Region region;

        public Builder() {
        }

        public String getUsername() {
            return username;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Region getRegion() {
            return region;
        }

        public Builder setRegion(Region region) {
            this.region = region;
            return this;
        }

        public DateTime getPublicationDate() {
            return publicationDate;
        }

        public Builder setPublicationDate(DateTime publicationDate) {
            this.publicationDate = publicationDate;
            return this;
        }

        public Brand getBrand() {
            return brand;
        }

        public Builder setBrand(Brand brand) {
            this.brand = brand;
            return this;
        }

        @Override
        public Details build() {
            return new Details(this);
        }
    }

    private Details(@NotNull Builder builder) {
        Preconditions.checkNotNull(builder);
        // todo validation
        this.username = builder.getUsername();
        this.region = builder.getRegion();
        this.publicationDate = builder.getPublicationDate();
        this.brand = builder.getBrand();
    }

    private Details(Parcel in) {
        username = in.readString();
        brand = in.readParcelable(Brand.class.getClassLoader());
        region = in.readParcelable(Region.class.getClassLoader());
        publicationDate = (DateTime) in.readSerializable();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeParcelable(brand, flags);
        dest.writeParcelable(region, flags);
        dest.writeSerializable(publicationDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Details> CREATOR = new Creator<Details>() {
        @Override
        public Details createFromParcel(Parcel in) {
            return new Details(in);
        }

        @Override
        public Details[] newArray(int size) {
            return new Details[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public Region getRegion() {
        return region;
    }

    public DateTime getPublicationDate() {
        return publicationDate;
    }

    public Brand getBrand() {
        return brand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Details details = (Details) o;

        if (!username.equals(details.username)) return false;
        if (!brand.equals(details.brand)) return false;
        if (!region.equals(details.region)) return false;
        return publicationDate.equals(details.publicationDate);

    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + brand.hashCode();
        result = 31 * result + region.hashCode();
        result = 31 * result + publicationDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Details{" +
                "username='" + username + '\'' +
                ", brand=" + brand +
                ", region=" + region +
                ", publicationDate=" + publicationDate +
                '}';
    }
}
