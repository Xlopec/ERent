package com.ua.erent.module.core.item.domain.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 11/7/2016.
 */

public final class ItemInfo implements Parcelable {

    private final String name;
    private final String description;
    private final BigDecimal price;

    public ItemInfo(String name, String description, BigDecimal price) {

        if(BigDecimal.ONE.compareTo(Preconditions.checkNotNull(price)) > 0)
            throw new IllegalArgumentException(String.format(
                    "price should be greater than 0, was %s", price));


        this.name = name;
        this.description = description;
        this.price = price;
    }

    private ItemInfo(Parcel in) {
        name = in.readString();
        description = in.readString();
        price = new BigDecimal(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(price.toPlainString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemInfo> CREATOR = new Creator<ItemInfo>() {
        @Override
        public ItemInfo createFromParcel(Parcel in) {
            return new ItemInfo(in);
        }

        @Override
        public ItemInfo[] newArray(int size) {
            return new ItemInfo[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemInfo itemInfo = (ItemInfo) o;

        if (!name.equals(itemInfo.name)) return false;
        if (!description.equals(itemInfo.description)) return false;
        return price.equals(itemInfo.price);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + price.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ItemInfo{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
