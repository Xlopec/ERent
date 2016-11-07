package com.ua.erent.module.core.item.domain.bo;

import com.ua.erent.module.core.account.auth.user.domain.vo.UserID;
import com.ua.erent.module.core.item.domain.vo.Details;
import com.ua.erent.module.core.item.domain.vo.ItemID;
import com.ua.erent.module.core.item.domain.vo.ItemInfo;
import com.ua.erent.module.core.util.IBuilder;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 11/7/2016.
 */

public final class Item {

    private final ItemID id;
    private final UserID owner;

    private ItemInfo itemInfo;
    private Details details;

    public static class Builder implements IBuilder<Item> {

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
}
