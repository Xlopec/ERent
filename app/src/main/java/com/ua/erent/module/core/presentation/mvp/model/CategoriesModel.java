package com.ua.erent.module.core.presentation.mvp.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Px;

import com.ua.erent.R;
import com.ua.erent.module.core.item.domain.ICategoryAppService;
import com.ua.erent.module.core.item.domain.bo.Category;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.ICategoriesModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
import com.ua.erent.module.core.presentation.mvp.view.util.IFutureBitmap;
import com.ua.erent.module.core.presentation.mvp.view.util.ImageUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Максим on 11/12/2016.
 */

public final class CategoriesModel implements ICategoriesModel {

    private final ICategoryAppService categoryAppService;
    private final Context context;

    @Inject
    public CategoriesModel(Context context, ICategoryAppService categoryAppService) {
        this.categoryAppService = categoryAppService;
        this.context  = context;
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

        for(final Category category : categories) {
            result.add(new CategoryModel(category.getId().getId(), category.getTitle(), category.getDescription(), new IFutureBitmap() {
                @NotNull
                @Override
                public Observable<Bitmap> fetch(@Px int width, @Px int height) {
                    return Observable.create(new Observable.OnSubscribe<Bitmap>() {
                        @Override
                        public void call(Subscriber<? super Bitmap> subscriber) {
                            final Bitmap bitmap = ImageUtils.decodeSampledBitmapFromResource(
                                    context.getResources(),
                                    R.drawable.bike_test,
                                    ImageUtils.pxToDp(width),
                                    ImageUtils.pxToDp(height));

                            subscriber.onNext(bitmap);
                            subscriber.onCompleted();
                        }
                    });
                }
            }));
        }

        return result;
    }

}
