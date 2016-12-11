package com.ua.erent.module.core.presentation.mvp.presenter;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Максим on 12/10/2016.
 */

public final class ItemCreationForm implements Parcelable {

    private String name;
    private String description;
    private double price;
    private long brandId;
    private long regionId;
    private long categoryId;
    private final ArrayList<Uri> uris;

    public ItemCreationForm() {
        uris = new ArrayList<>(0);
    }

    private ItemCreationForm(Parcel in) {
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
        brandId = in.readLong();
        regionId = in.readLong();
        categoryId = in.readLong();

        final Parcelable [] uriArr = in.readParcelableArray(Uri.class.getClassLoader());
        uris = new ArrayList<>(uriArr.length);

        for(final Parcelable uri : uriArr) {
            uris.add((Uri) uri);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeLong(brandId);
        dest.writeLong(regionId);
        dest.writeLong(categoryId);
        dest.writeTypedArray(uris.toArray(new Uri[uris.size()]), flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemCreationForm> CREATOR = new Creator<ItemCreationForm>() {
        @Override
        public ItemCreationForm createFromParcel(Parcel in) {
            return new ItemCreationForm(in);
        }

        @Override
        public ItemCreationForm[] newArray(int size) {
            return new ItemCreationForm[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getBrandId() {
        return brandId;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
    }

    public long getRegionId() {
        return regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public void addUri(@NotNull Uri uri) {
        uris.add(uri);
    }

    public void removeUri(@NotNull Uri uri) {
        uris.remove(uri);
    }

    public void removeUris() {
        uris.clear();
    }

    public Collection<Uri> getUris() {
        return Collections.unmodifiableCollection(uris);
    }
}
