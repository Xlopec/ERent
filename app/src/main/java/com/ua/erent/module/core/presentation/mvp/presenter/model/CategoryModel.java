package com.ua.erent.module.core.presentation.mvp.presenter.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ua.erent.module.core.presentation.mvp.view.util.IFutureBitmap;
import com.ua.erent.module.core.presentation.mvp.view.util.IParcelableFutureBitmap;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 11/12/2016.
 */

public final class CategoryModel implements Parcelable {

    private final long id;
    private final String title;
    private final String description;
    private final IParcelableFutureBitmap futureBitmap;

    public CategoryModel(long id, String title, String description, IParcelableFutureBitmap futureBitmap) {
        this.id = id;
        this.title = Preconditions.checkNotNull(title);
        this.description = Preconditions.checkNotNull(description);
        this.futureBitmap = futureBitmap;
    }

    private CategoryModel(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
        futureBitmap = in.readParcelable(IParcelableFutureBitmap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeParcelable(futureBitmap, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryModel> CREATOR = new Creator<CategoryModel>() {
        @Override
        public CategoryModel createFromParcel(Parcel in) {
            return new CategoryModel(in);
        }

        @Override
        public CategoryModel[] newArray(int size) {
            return new CategoryModel[size];
        }
    };

    public IFutureBitmap getFutureBitmap() {
        return futureBitmap;
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
}
