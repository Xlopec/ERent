package com.ua.erent.module.core.presentation.mvp.presenter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Px;

import com.ua.erent.R;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.ICategoriesModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ICategoriesPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
import com.ua.erent.module.core.presentation.mvp.view.CategoriesActivity;
import com.ua.erent.module.core.presentation.mvp.view.util.IFutureBitmap;
import com.ua.erent.module.core.presentation.mvp.view.util.ImageUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Максим on 11/12/2016.
 */

public final class CategoriesPresenter extends ICategoriesPresenter {

    private static final int[] CATEGORY_COLORS = {
            0xcc0000,// red
            0x333399,// blue
            0xff9900,//orange
            0x339933,//green
            0x00cccc//cyan
    };

    @Inject
    public CategoriesPresenter(ICategoriesModel model) {
    }

    @Override
    protected void onViewAttached(@NotNull CategoriesActivity view, @Nullable Bundle savedState, @Nullable Bundle data) {

        CategoryModel model1 = new CategoryModel(2, "Bike", "Bike category", new IFutureBitmap() {
            @NotNull
            @Override
            public Observable<Bitmap> fetch(@Px int width, @Px int height) {
                return Observable.create(new Observable.OnSubscribe<Bitmap>() {
                    @Override
                    public void call(Subscriber<? super Bitmap> subscriber) {
                        final Bitmap bitmap = ImageUtils.decodeSampledBitmapFromResource(
                                view.getContext().getResources(),
                                R.drawable.bike_test,
                                ImageUtils.pxToDp(width),
                                ImageUtils.pxToDp(height));

                        subscriber.onNext(bitmap);
                        subscriber.onCompleted();
                    }
                });
            }
        });

        CategoryModel model2 = new CategoryModel(2, "Bike", "Bike category", new IFutureBitmap() {
            @NotNull
            @Override
            public Observable<Bitmap> fetch(@Px int width, @Px int height) {
                return Observable.create(new Observable.OnSubscribe<Bitmap>() {
                    @Override
                    public void call(Subscriber<? super Bitmap> subscriber) {
                        final Bitmap bitmap = ImageUtils.decodeSampledBitmapFromResource(
                                view.getContext().getResources(),
                                R.drawable.nav_drawer_bg,
                                ImageUtils.pxToDp(width),
                                ImageUtils.pxToDp(height));

                        subscriber.onNext(bitmap);
                        subscriber.onCompleted();
                    }
                });
            }
        });

        CategoryModel model3 = new CategoryModel(3, "Bike", "Bike category", new IFutureBitmap() {
            @NotNull
            @Override
            public Observable<Bitmap> fetch(@Px int width, @Px int height) {
                return Observable.create(new Observable.OnSubscribe<Bitmap>() {
                    @Override
                    public void call(Subscriber<? super Bitmap> subscriber) {
                        final Bitmap bitmap = ImageUtils.decodeSampledBitmapFromResource(
                                view.getContext().getResources(),
                                R.drawable.bg_test1,
                                ImageUtils.pxToDp(width),
                                ImageUtils.pxToDp(height));
                        subscriber.onNext(bitmap);
                        subscriber.onCompleted();
                    }
                });
            }
        });

        CategoryModel model4 = new CategoryModel(3, "Bike", "Bike category", new IFutureBitmap() {
            @NotNull
            @Override
            public Observable<Bitmap> fetch(@Px int width, @Px int height) {
                return Observable.create(new Observable.OnSubscribe<Bitmap>() {
                    @Override
                    public void call(Subscriber<? super Bitmap> subscriber) {
                        final Bitmap bitmap = ImageUtils.decodeSampledBitmapFromResource(
                                view.getContext().getResources(),
                                R.drawable.bg_test1,
                                ImageUtils.pxToDp(width),
                                ImageUtils.pxToDp(height));
                        subscriber.onNext(bitmap);
                        subscriber.onCompleted();
                    }
                });
            }
        });

        CategoryModel model5 = new CategoryModel(3, "Bike", "Bike category", new IFutureBitmap() {
            @NotNull
            @Override
            public Observable<Bitmap> fetch(@Px int width, @Px int height) {
                return Observable.create(new Observable.OnSubscribe<Bitmap>() {
                    @Override
                    public void call(Subscriber<? super Bitmap> subscriber) {

                        final Bitmap bitmap = ImageUtils.decodeSampledBitmapFromResource(
                                view.getContext().getResources(),
                                R.drawable.nav_drawer_bg,
                                ImageUtils.pxToDp(width),
                                ImageUtils.pxToDp(height));
                        subscriber.onNext(bitmap);
                        subscriber.onCompleted();
                    }
                });
            }
        });

        view.addCategory(model1);
        view.addCategory(model2);
        view.addCategory(model3);
        view.addCategory(model4);
        view.addCategory(model5);
    }

    @Override
    protected void onDestroyed() {

    }

    @Override
    public int getRandomColor() {
        return CATEGORY_COLORS[new Random().nextInt(CATEGORY_COLORS.length)];
    }

    @Override
    public void onOpenCategory(int categoryId) {

    }

    @Override
    public void onRefresh() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> getView().hideRefreshProgress(), 3000L);
    }
}
