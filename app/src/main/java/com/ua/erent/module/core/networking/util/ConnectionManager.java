package com.ua.erent.module.core.networking.util;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

/**
 * <p>
 * An utility class to check the internet connection
 * and register callback to monitor connection state
 * </p>
 * Created by Максим on 9/1/2016.
 */
@Singleton
public final class ConnectionManager {

    private final PublishSubject<Boolean> networkObs;
    private boolean hasConnection;

    @Inject
    public ConnectionManager(@NotNull Application application) {

        final ConnectivityManager connectivityManager = (ConnectivityManager)
                application.getSystemService(Context.CONNECTIVITY_SERVICE);

        networkObs = PublishSubject.create();
        hasConnection = connectivityManager.getActiveNetworkInfo() != null;

        final BroadcastReceiver receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                final Bundle bundle = intent.getExtras();

                if (bundle != null) {

                    final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    final boolean isConnected = networkInfo != null && networkInfo.isConnected();

                    synchronized (networkObs) {

                        if (isConnected != hasConnection) {
                            networkObs.onNext(hasConnection = isConnected);
                        }
                    }
                }
            }
        };

        application.registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public boolean hasConnection() {
        return hasConnection;
    }

    public Observable<Boolean> getNetworkObservable() {
        return networkObs.asObservable().observeOn(AndroidSchedulers.mainThread());
    }

}
