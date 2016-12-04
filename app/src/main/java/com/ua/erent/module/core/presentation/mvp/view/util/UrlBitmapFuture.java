package com.ua.erent.module.core.presentation.mvp.view.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.support.annotation.Px;
import android.support.v4.util.LruCache;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import dagger.internal.Preconditions;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Максим on 11/28/2016.
 */
public class UrlBitmapFuture implements IUrlFutureBitmap {

    private final MyURL url;
    private final LruCache<Integer, Bitmap> cache;

    public static final Creator<UrlBitmapFuture> CREATOR = new Creator<UrlBitmapFuture>() {
        @Override
        public UrlBitmapFuture createFromParcel(Parcel in) {
            return new UrlBitmapFuture(in);
        }

        @Override
        public UrlBitmapFuture[] newArray(int size) {
            return new UrlBitmapFuture[size];
        }
    };

    UrlBitmapFuture(@NotNull MyURL url) {
        this.url = Preconditions.checkNotNull(url);
        this.cache = new LruCache<>(1);
    }

    UrlBitmapFuture(@NotNull URL url) {
        this.url = new MyURL(Preconditions.checkNotNull(url));
        this.cache = new LruCache<>(1);
    }

    private UrlBitmapFuture(Parcel in) {
        this.cache = new LruCache<>(1);
        this.url = in.readParcelable(MyURL.class.getClassLoader());
    }

    @NotNull
    public MyURL getUrl() {
        return url;
    }

    @NotNull
    @Override
    public Observable<Bitmap> fetch(@Px int reqW, @Px int reqH, @NotNull Context context) {

        return cache.get(1) == null ? Observable.defer(() ->
                Observable.create((Observable.OnSubscribe<Bitmap>) subscriber -> {

                    subscriber.onStart();
                    HttpURLConnection connection = null;

                    try {

                        connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();

                        InputStream is = null;

                        try {

                            is = connection.getInputStream();
                            final Bitmap rawBm = BitmapFactory.decodeStream(is);
                            int outW = rawBm.getWidth(), outH = rawBm.getHeight();

                            while (outW / 2 > reqW && outH / 2 > outH) {
                                outW /= 2;
                                outH /= 2;
                            }

                            final Bitmap bitmap = Bitmap.createScaledBitmap(rawBm, outW, outH, true);

                            cache.put(0, bitmap);
                            subscriber.onNext(bitmap);
                        } finally {
                            if (is != null) is.close();
                        }
                    } catch (final IOException e) {
                        subscriber.onError(e);
                    } finally {
                        if (connection != null) connection.disconnect();
                        subscriber.onCompleted();
                    }
                }))
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                : Observable.just(cache.get(1));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(url, flags);
    }
}
