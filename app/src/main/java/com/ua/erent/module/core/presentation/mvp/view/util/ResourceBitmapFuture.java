package com.ua.erent.module.core.presentation.mvp.view.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.support.annotation.Px;

import com.ua.erent.R;

import org.jetbrains.annotations.NotNull;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Максим on 11/28/2016.
 */
public class ResourceBitmapFuture implements IParcelableFutureBitmap {

    private final int resourceId;
    private Bitmap bitmap;

    public static final Creator<ResourceBitmapFuture> CREATOR = new Creator<ResourceBitmapFuture>() {
        @Override
        public ResourceBitmapFuture createFromParcel(Parcel in) {
            return new ResourceBitmapFuture(in);
        }

        @Override
        public ResourceBitmapFuture[] newArray(int size) {
            return new ResourceBitmapFuture[size];
        }
    };

    ResourceBitmapFuture(int resourceId) {
        this.resourceId = resourceId;
    }

    private ResourceBitmapFuture(Parcel in) {
        resourceId = in.readInt();
    }

    @NotNull
    @Override
    public Observable<Bitmap> fetch(@Px int width, @Px int height, @NotNull Context context) {

        return bitmap == null ? Observable.defer(() ->
                Observable.create((Observable.OnSubscribe<Bitmap>) subscriber -> {

                    subscriber.onStart();

                    try {
                        bitmap = ImageUtils.decodeSampledBitmapFromResource(
                                context.getResources(),
                                R.drawable.bike_test,
                                ImageUtils.pxToDp(width),
                                ImageUtils.pxToDp(height));
                        subscriber.onNext(bitmap);
                    } finally {
                        subscriber.onCompleted();
                    }
                }))
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                : Observable.just(bitmap);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        bitmap = null;
        dest.writeInt(resourceId);
    }
}
