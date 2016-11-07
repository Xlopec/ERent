package com.ua.erent.module.core.item.domain.vo;

import com.ua.erent.module.core.util.IBuilder;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 11/7/2016.
 */

public final class Details {

    private final Brand brand;
    private final Region region;
    private final DateTime publicationDate;
    private final boolean isVisible;

    public final static class Builder implements IBuilder<Details> {

        private Brand brand;
        private DateTime publicationDate;
        private boolean isVisible;
        private Region region;

        public Builder() {
        }

        public boolean isVisible() {
            return isVisible;
        }

        public Builder setVisible(boolean visible) {
            isVisible = visible;
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
        this.isVisible = builder.isVisible();
        this.region = builder.getRegion();
        this.publicationDate = builder.getPublicationDate();
        this.brand = builder.getBrand();
    }

    public boolean isVisible() {
        return isVisible;
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
}
