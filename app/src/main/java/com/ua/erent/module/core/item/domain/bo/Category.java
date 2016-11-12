package com.ua.erent.module.core.item.domain.bo;

import android.os.Parcel;
import android.os.Parcelable;

import com.ua.erent.module.core.item.domain.vo.CategoryID;

import org.jetbrains.annotations.NotNull;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 11/12/2016.
 */

public final class Category implements Parcelable {

    private final CategoryID id;
    private final String title;

    public Category(@NotNull String title, @NotNull CategoryID id) {
        this.title = Preconditions.checkNotNull(title);
        this.id = Preconditions.checkNotNull(id);
    }

    private Category(Parcel in) {
        id = in.readParcelable(CategoryID.class.getClassLoader());
        title = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public CategoryID getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(id, flags);
        dest.writeString(title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (!id.equals(category.id)) return false;
        return title.equals(category.title);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + title.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
