package com.ua.erent.module.core.item.domain.bo;

import android.os.Parcel;
import android.os.Parcelable;

import com.ua.erent.module.core.item.domain.vo.Details;
import com.ua.erent.module.core.item.domain.vo.ItemID;
import com.ua.erent.module.core.item.domain.vo.ItemInfo;
import com.ua.erent.module.core.util.IBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 11/7/2016.
 */

public final class Item implements Parcelable {

    private final ItemID id;
    // todo add update logic
    private final ArrayList<Category> categories;
    private ItemInfo itemInfo;
    private Details details;

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public static final class Builder implements IBuilder<Item> {

        private ItemID id;
        private ItemInfo itemInfo;
        private Details details;
        private Collection<Category> categories;

        public Builder() {
        }

        public ItemID getId() {
            return id;
        }

        public Builder setId(ItemID id) {
            this.id = id;
            return this;
        }

        public ItemInfo getItemInfo() {
            return itemInfo;
        }

        public Builder setItemInfo(ItemInfo itemInfo) {
            this.itemInfo = itemInfo;
            return this;
        }

        public Details getDetails() {
            return details;
        }

        public Builder setDetails(Details details) {
            this.details = details;
            return this;
        }

        public Collection<Category> getCategories() {
            return categories;
        }

        public Builder setCategories(Collection<Category> categories) {
            this.categories = categories == null ? null : new ArrayList<>(categories);
            return this;
        }

        @Override
        public Item build() {
            return new Item(this);
        }
    }

    private Item(Builder builder) {
        Preconditions.checkNotNull(builder);
        Preconditions.checkNotNull(builder.getCategories());

        if (builder.getCategories().isEmpty())
            throw new IllegalArgumentException("item should have at least one category");

        this.id = Preconditions.checkNotNull(builder.getId());
        this.itemInfo = Preconditions.checkNotNull(builder.getItemInfo());
        this.details = Preconditions.checkNotNull(builder.getDetails());
        this.categories = new ArrayList<>(builder.getCategories());
    }

    private Item(Parcel in) {
        id = in.readParcelable(ItemID.class.getClassLoader());
        itemInfo = in.readParcelable(ItemInfo.class.getClassLoader());
        details = in.readParcelable(Details.class.getClassLoader());

        categories = new ArrayList<>();
        in.readTypedList(categories, Category.CREATOR);
        categories.trimToSize();
    }

    public ItemID getId() {
        return id;
    }

    public ItemInfo getItemInfo() {
        return itemInfo;
    }

    public Details getDetails() {
        return details;
    }

    public Collection<Category> getCategories() {
        return Collections.unmodifiableCollection(categories);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(id, flags);
        dest.writeParcelable(itemInfo, flags);
        dest.writeParcelable(details, flags);
        dest.writeTypedList(categories);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (!id.equals(item.id)) return false;
        if (!categories.equals(item.categories)) return false;
        if (!itemInfo.equals(item.itemInfo)) return false;
        return details.equals(item.details);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + categories.hashCode();
        result = 31 * result + itemInfo.hashCode();
        result = 31 * result + details.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", categories=" + categories +
                ", itemInfo=" + itemInfo +
                ", details=" + details +
                '}';
    }
}
