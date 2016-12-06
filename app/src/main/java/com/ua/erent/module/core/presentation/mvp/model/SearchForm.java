package com.ua.erent.module.core.presentation.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by Максим on 12/6/2016.
 */

public final class SearchForm implements Parcelable {

    private final HashSet<Long> categories;
    private final HashSet<Long> brands;
    private final HashSet<Long> regions;
    private int priceFrom;
    private int priceTo;
    private String query;

    public SearchForm() {
        categories = new HashSet<>(0);
        brands = new HashSet<>(0);
        regions = new HashSet<>(0);
        priceFrom = priceTo = -1;
    }


    private SearchForm(Parcel in) {
        query = in.readString();
        categories = (HashSet<Long>) in.readSerializable();
        brands = (HashSet<Long>) in.readSerializable();
        regions = (HashSet<Long>) in.readSerializable();
        priceFrom = in.readInt();
        priceTo = in.readInt();
    }

    public static final Creator<SearchForm> CREATOR = new Creator<SearchForm>() {
        @Override
        public SearchForm createFromParcel(Parcel in) {
            return new SearchForm(in);
        }

        @Override
        public SearchForm[] newArray(int size) {
            return new SearchForm[size];
        }
    };

    public Collection<Long> getCategories() {
        return Collections.unmodifiableCollection(categories);
    }

    public Collection<Long> getBrands() {
        return Collections.unmodifiableCollection(brands);
    }

    public Collection<Long> getRegions() {
        return Collections.unmodifiableCollection(regions);
    }

    public void addCategory(long id) {
        categories.add(id);
    }

    public void removeCategory(long id) {
        categories.remove(id);
    }

    public void addBrand(long id) {
        brands.add(id);
    }

    public void removeBrand(long id) {
        brands.remove(id);
    }

    public void addRegion(long id) {
        regions.add(id);
    }

    public void removeRegion(long id) {
        regions.remove(id);
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public int getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(int priceFrom) {
        this.priceFrom = priceFrom;
    }

    public int getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(int priceTo) {
        this.priceTo = priceTo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(query);
        dest.writeSerializable(categories);
        dest.writeSerializable(brands);
        dest.writeSerializable(regions);
        dest.writeInt(priceFrom);
        dest.writeInt(priceTo);
    }
}
