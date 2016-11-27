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

    private final UserInfo userInfo;
    private final Brand brand;
    private final Region region;
    private final DateTime publicationDate;

    public final static class Builder implements IBuilder<Details> {

        private UserInfo userInfo;
        private Brand brand;
        private DateTime publicationDate;
        private Region region;

        public Builder() {
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

        public UserInfo getUserInfo() {
            return userInfo;
        }

        public Builder setUserInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
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
        this.userInfo = Preconditions.checkNotNull(builder.getUserInfo());
        this.region = Preconditions.checkNotNull(builder.getRegion());
        this.publicationDate = builder.getPublicationDate();
        this.brand = Preconditions.checkNotNull(builder.getBrand());
    }

    private Details(Parcel in) {
        userInfo = in.readParcelable(UserInfo.class.getClassLoader());
        brand = in.readParcelable(Brand.class.getClassLoader());
        region = in.readParcelable(Region.class.getClassLoader());
        publicationDate = (DateTime) in.readSerializable();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(userInfo, flags);
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

    public UserInfo getUserInfo() {
        return userInfo;
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

        if (!userInfo.equals(details.userInfo)) return false;
        if (!brand.equals(details.brand)) return false;
        if (!region.equals(details.region)) return false;
        return publicationDate.equals(details.publicationDate);

    }

    @Override
    public int hashCode() {
        int result = userInfo.hashCode();
        result = 31 * result + brand.hashCode();
        result = 31 * result + region.hashCode();
        result = 31 * result + publicationDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Details{" +
                "userInfo='" + userInfo + '\'' +
                ", brand=" + brand +
                ", region=" + region +
                ", publicationDate=" + publicationDate +
                '}';
    }
}
