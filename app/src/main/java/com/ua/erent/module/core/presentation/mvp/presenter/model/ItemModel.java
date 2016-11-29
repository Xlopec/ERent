package com.ua.erent.module.core.presentation.mvp.presenter.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ua.erent.module.core.presentation.mvp.view.util.IParcelableFutureBitmap;
import com.ua.erent.module.core.util.IBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 11/28/2016.
 */

public final class ItemModel implements Parcelable {

    private final long id;
    private final String username;
    private final IParcelableFutureBitmap userAvatar;
    private final ArrayList<IParcelableFutureBitmap> gallery;
    private final String price;
    private final String title;
    private final String description;
    private final String pubDate;
    private final ArrayList<String> categories;
    private final String brand;
    private final String region;

    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };

    public static final class Builder implements IBuilder<ItemModel> {

        private final long id;
        private final String username;
        private final String title;
        private final String description;
        private final String pubDate;
        private final String price;
        private final Collection<String> categories;
        private final String brand;
        private final String region;

        private IParcelableFutureBitmap userAvatar;
        private final Collection<IParcelableFutureBitmap> gallery;

        public Builder(long id, String username, String title, String description,
                       String pubDate, String price, Collection<String> categories,
                       String brand, String region) {

            this.id = id;
            this.username = username;
            this.title = title;
            this.description = description;
            this.pubDate = pubDate;
            this.price = price;
            this.categories = categories;
            this.brand = brand;
            this.region = region;
            this.gallery = new ArrayList<>(0);
        }

        public String getUsername() {
            return username;
        }

        public long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getPubDate() {
            return pubDate;
        }

        public String getPrice() {
            return price;
        }

        public Collection<String> getCategories() {
            return categories;
        }

        public String getBrand() {
            return brand;
        }

        public String getRegion() {
            return region;
        }

        public IParcelableFutureBitmap getUserAvatar() {
            return userAvatar;
        }

        public Builder setUserAvatar(IParcelableFutureBitmap userAvatar) {
            this.userAvatar = userAvatar;
            return this;
        }

        public Collection<IParcelableFutureBitmap> getGallery() {
            return Collections.unmodifiableCollection(gallery);
        }

        public Builder addGallery(@NotNull Collection<IParcelableFutureBitmap> gallery) {
            this.gallery.addAll(Preconditions.checkNotNull(gallery));
            return this;
        }

        @Override
        public ItemModel build() {
            return new ItemModel(this);
        }
    }

    private ItemModel(Builder builder) {
        this.id = builder.getId();
        this.username = builder.getUsername();
        this.brand = builder.getBrand();
        this.categories = new ArrayList<>(builder.getCategories());
        this.region = builder.getRegion();
        this.title = builder.getTitle();
        this.pubDate = builder.getPubDate();
        this.description = builder.getDescription();
        this.price = builder.getPrice();
        this.gallery = new ArrayList<>(builder.getGallery());
        this.userAvatar = builder.getUserAvatar();
    }

    private ItemModel(Parcel in) {
        id = in.readLong();
        userAvatar = in.readParcelable(IParcelableFutureBitmap.class.getClassLoader());
        final IParcelableFutureBitmap[] galleryArr =
                (IParcelableFutureBitmap[]) in.readParcelableArray(IParcelableFutureBitmap.class.getClassLoader());
        gallery = new ArrayList<>(galleryArr.length);
        Collections.addAll(gallery, galleryArr);
        price = in.readString();
        title = in.readString();
        description = in.readString();
        pubDate = in.readString();
        categories = in.createStringArrayList();
        brand = in.readString();
        region = in.readString();
        username = in.readString();
    }

    public long getId() {
        return id;
    }

    public IParcelableFutureBitmap getUserAvatar() {
        return userAvatar;
    }

    public Collection<IParcelableFutureBitmap> getGallery() {
        return gallery;
    }

    public String getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public Collection<String> getCategories() {
        return categories;
    }

    public String getBrand() {
        return brand;
    }

    public String getRegion() {
        return region;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeParcelable(userAvatar, flags);
        dest.writeParcelableArray(gallery.toArray(new IParcelableFutureBitmap[gallery.size()]), flags);
        dest.writeString(price);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(pubDate);
        dest.writeStringList(categories);
        dest.writeString(brand);
        dest.writeString(region);
        dest.writeString(username);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemModel itemModel = (ItemModel) o;

        if (id != itemModel.id) return false;
        if (userAvatar != null ? !userAvatar.equals(itemModel.userAvatar) : itemModel.userAvatar != null)
            return false;
        if (!gallery.equals(itemModel.gallery)) return false;
        if (!price.equals(itemModel.price)) return false;
        if (!username.equals(itemModel.username)) return false;
        if (!title.equals(itemModel.title)) return false;
        if (!description.equals(itemModel.description)) return false;
        if (!pubDate.equals(itemModel.pubDate)) return false;
        if (!categories.equals(itemModel.categories)) return false;
        if (!brand.equals(itemModel.brand)) return false;
        return region.equals(itemModel.region);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (userAvatar != null ? userAvatar.hashCode() : 0);
        result = 31 * result + gallery.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + pubDate.hashCode();
        result = 31 * result + categories.hashCode();
        result = 31 * result + brand.hashCode();
        result = 31 * result + region.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ItemModel{" +
                "id=" + id +
                ", username=" + username +
                ", userAvatar=" + userAvatar +
                ", gallery=" + gallery +
                ", price='" + price + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", categories=" + categories +
                ", brand='" + brand + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}
