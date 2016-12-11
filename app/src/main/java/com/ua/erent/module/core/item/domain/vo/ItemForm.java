package com.ua.erent.module.core.item.domain.vo;

import android.net.Uri;

import com.ua.erent.module.core.util.IBuilder;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 12/9/2016.
 */
public final class ItemForm {

    private final String name;
    private final String description;
    private final BigDecimal price;
    private final long brandId;
    private final long regionId;
    private final Collection<Long> categoryIds;
    private final Collection<Uri> uris;

    public static final class Builder implements IBuilder<ItemForm> {

        private final String name;
        private final String description;
        private final BigDecimal price;
        private final long brandId;
        private final long regionId;
        private final Collection<Long> categoryIds;
        private final Collection<Uri> uris;

        public Builder(String name, String description, BigDecimal price, long brandId, long regionId) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.brandId = brandId;
            this.regionId = regionId;
            this.categoryIds = new ArrayList<>(1);
            this.uris = new ArrayList<>(0);
        }

        public Builder addCategory(long id) {
            this.categoryIds.add(id);
            return this;
        }

        public Builder removeCategory(long id) {
            this.categoryIds.remove(id);
            return this;
        }

        public Builder addImage(@NotNull Uri uri) {
            this.uris.add(Preconditions.checkNotNull(uri));
            return this;
        }

        public Builder removeImage(@NotNull Uri uri) {
            this.uris.remove(Preconditions.checkNotNull(uri));
            return this;
        }

        public Collection<Uri> getUris() {
            return Collections.unmodifiableCollection(uris);
        }

        public Collection<Long> getCategoryIds() {
            return Collections.unmodifiableCollection(categoryIds);
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public long getBrandId() {
            return brandId;
        }

        public long getRegionId() {
            return regionId;
        }

        @Override
        public ItemForm build() {
            return new ItemForm(this);
        }

        public Builder addImage(@NotNull Collection<Uri> uris) {
            this.uris.addAll(Preconditions.checkNotNull(uris));
            return this;
        }
    }

    public ItemForm(Builder builder) {
        Preconditions.checkNotNull(builder);
        this.name = builder.getName();
        this.description = builder.getDescription();
        this.price = builder.getPrice();
        this.brandId = builder.getBrandId();
        this.regionId = builder.getRegionId();
        this.categoryIds = new ArrayList<>(builder.getCategoryIds());
        this.uris = new ArrayList<>(builder.getUris());
    }

    public Collection<Uri> getUris() {
        return Collections.unmodifiableCollection(uris);
    }

    public Collection<Long> getCategoryIds() {
        return Collections.unmodifiableCollection(categoryIds);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getBrandId() {
        return brandId;
    }

    public long getRegionId() {
        return regionId;
    }

    @Override
    public String toString() {
        return "ItemForm{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", brandId=" + brandId +
                ", regionId=" + regionId +
                ", categoryIds=" + categoryIds +
                ", uris=" + uris +
                '}';
    }
}
