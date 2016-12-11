package com.ua.erent.module.core.presentation.mvp.view.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.support.annotation.Px;
import android.support.v4.util.LruCache;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

import dagger.internal.Preconditions;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Максим on 12/10/2016.
 */

public final class UriBitmap implements IParcelableFutureBitmap {

    private final Uri uri;
    private final LruCache<Integer, Bitmap> cache;

    public static final Creator<UriBitmap> CREATOR = new Creator<UriBitmap>() {
        @Override
        public UriBitmap createFromParcel(Parcel in) {
            return new UriBitmap(in);
        }

        @Override
        public UriBitmap[] newArray(int size) {
            return new UriBitmap[size];
        }
    };

    public UriBitmap(@NotNull Uri uri) {
        this.uri = Preconditions.checkNotNull(uri);
        this.cache = new LruCache<>(1);
    }

    private UriBitmap(Parcel in) {
        uri = in.readParcelable(Uri.class.getClassLoader());
        this.cache = new LruCache<>(1);
    }

    public Uri getUri() {
        return uri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(uri, flags);
    }

    @NotNull
    @Override
    public Observable<Bitmap> fetch(@Px int width, @Px int height, @NotNull Context context) {
        return cache.get(0) == null ?
                Observable.defer(() -> Observable.create((Observable.OnSubscribe<Bitmap>) subscriber -> {

                    subscriber.onStart();
                    InputStream input = null;

                    try {

                        input = context.getContentResolver().openInputStream(uri);

                        try {

                            final Bitmap rawBm = BitmapFactory.decodeStream(input);
                            int outW = rawBm.getWidth(), outH = rawBm.getHeight();

                            while (outW / 2 > width && outH / 2 > height) {
                                outW /= 2;
                                outH /= 2;
                            }

                            final Bitmap bitmap = Bitmap.createScaledBitmap(rawBm, outW, outH, true);

                            cache.put(0, bitmap);
                            subscriber.onNext(bitmap);

                        } finally {
                            if (input != null) input.close();
                        }

                    } catch (final Exception e) {
                        subscriber.onError(e);
                    } finally {
                        subscriber.onCompleted();
                    }
                }))
                        .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                : Observable.just(cache.get(0));
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }

}
