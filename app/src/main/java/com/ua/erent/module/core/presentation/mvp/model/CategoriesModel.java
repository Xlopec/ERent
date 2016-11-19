package com.ua.erent.module.core.presentation.mvp.model;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.support.annotation.Px;

import com.ua.erent.R;
import com.ua.erent.module.core.item.domain.ICategoryAppService;
import com.ua.erent.module.core.item.domain.bo.Category;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.ICategoriesModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
import com.ua.erent.module.core.presentation.mvp.view.util.IParcelableFutureBitmap;
import com.ua.erent.module.core.presentation.mvp.view.util.ImageUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Максим on 11/12/2016.
 */

public final class CategoriesModel implements ICategoriesModel {

    private final ICategoryAppService categoryAppService;
    private final Application context;

    private static class BitmapHolder implements IParcelableFutureBitmap {

        private final int resourceId;
        private Bitmap bitmap;

        public static final Creator<BitmapHolder> CREATOR = new Creator<BitmapHolder>() {
            @Override
            public BitmapHolder createFromParcel(Parcel in) {
                return new BitmapHolder(in);
            }

            @Override
            public BitmapHolder[] newArray(int size) {
                return new BitmapHolder[size];
            }
        };

        BitmapHolder(int resourceId) {
            this.resourceId = resourceId;
        }

        private BitmapHolder(Parcel in) {
            resourceId = in.readInt();
        }

        @NotNull
        @Override
        public Observable<Bitmap> fetch(@Px int width, @Px int height, @NotNull Context context) {

            return bitmap == null ? Observable.create(new Observable.OnSubscribe<Bitmap>() {

                @Override
                public void call(Subscriber<? super Bitmap> subscriber) {

                    bitmap = ImageUtils.decodeSampledBitmapFromResource(
                            context.getResources(),
                            R.drawable.bike_test,
                            ImageUtils.pxToDp(width),
                            ImageUtils.pxToDp(height));

                    subscriber.onNext(bitmap);
                    subscriber.onCompleted();
                }
            }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                    : Observable.just(bitmap);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(resourceId);
        }
    }

    @Inject
    public CategoriesModel(Application context, ICategoryAppService categoryAppService) {
        this.categoryAppService = categoryAppService;
        this.context = context;
    }

    @Override
    public Collection<CategoryModel> getCategories() {
        return toModel(categoryAppService.getCachedCategories());
    }

    @Override
    public Observable<Collection<CategoryModel>> getOnCategoriesDeletedObs() {
        return null;
    }

    @Override
    public Observable<Collection<CategoryModel>> fetchCategories() {
        return categoryAppService.fetchCategories().map(this::toModel);
    }

    @Override
    public Observable<Collection<CategoryModel>> getOnCategoriesAddedObs() {
        return null;
    }

    private Collection<CategoryModel> toModel(Collection<Category> categories) {

        final Collection<CategoryModel> result = new ArrayList<>(categories.size());

        for (final Category category : categories) {
            result.add(new CategoryModel(category.getId().getId(), category.getTitle(),
                    category.getDescription(), new BitmapHolder(R.drawable.bike_test)));
        }

        return result;
    }

}
