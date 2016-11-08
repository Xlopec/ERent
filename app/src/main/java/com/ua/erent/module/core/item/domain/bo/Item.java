package com.ua.erent.module.core.item.domain.bo;

import android.os.Parcel;
import android.os.Parcelable;

import com.ua.erent.module.core.account.auth.user.domain.vo.UserID;
import com.ua.erent.module.core.item.domain.vo.Details;
import com.ua.erent.module.core.item.domain.vo.ItemID;
import com.ua.erent.module.core.item.domain.vo.ItemInfo;
import com.ua.erent.module.core.util.IBuilder;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 11/7/2016.
 */

public final class Item implements Parcelable {

    private final ItemID id;
    private final UserID owner;
    // todo add update logic
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
        private UserID owner;
        private ItemInfo itemInfo;
        private Details details;

        public Builder() {
        }

        public ItemID getId() {
            return id;
        }

        public Builder setId(ItemID id) {
            this.id = id;
            return this;
        }

        public UserID getOwner() {
            return owner;
        }

        public Builder setOwner(UserID owner) {
            this.owner = owner;
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

        @Override
        public Item build() {
            return new Item(this);
        }
    }

    private Item(Builder builder) {
        Preconditions.checkNotNull(builder);
        this.id = Preconditions.checkNotNull(builder.getId());
        this.owner = Preconditions.checkNotNull(builder.getOwner());
        this.itemInfo = Preconditions.checkNotNull(builder.getItemInfo());
        this.details = Preconditions.checkNotNull(builder.getDetails());
    }

    private Item(Parcel in) {
        id = in.readParcelable(ItemID.class.getClassLoader());
        owner = in.readParcelable(UserID.class.getClassLoader());
        itemInfo = in.readParcelable(ItemInfo.class.getClassLoader());
        details = in.readParcelable(Details.class.getClassLoader());
    }

    public ItemID getId() {
        return id;
    }

    public UserID getOwner() {
        return owner;
    }

    public ItemInfo getItemInfo() {
        return itemInfo;
    }

    public Details getDetails() {
        return details;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(id, flags);
        dest.writeParcelable(owner, flags);
        dest.writeParcelable(itemInfo, flags);
        dest.writeParcelable(details, flags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (!id.equals(item.id)) return false;
        if (!owner.equals(item.owner)) return false;
        if (!itemInfo.equals(item.itemInfo)) return false;
        return details.equals(item.details);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + owner.hashCode();
        result = 31 * result + itemInfo.hashCode();
        result = 31 * result + details.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", owner=" + owner +
                ", itemInfo=" + itemInfo +
                ", details=" + details +
                '}';
    }
}
