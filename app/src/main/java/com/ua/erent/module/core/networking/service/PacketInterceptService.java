package com.ua.erent.module.core.networking.service;

import org.jetbrains.annotations.NotNull;

import okhttp3.Response;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * Created by Максим on 10/21/2016.
 */

public final class PacketInterceptService implements IPacketInterceptService {

    private final PublishSubject<Response> subject;

    public PacketInterceptService() {
        subject = PublishSubject.create();
    }

    @Override
    public void intercept(@NotNull Response response) {
        subject.onNext(response);
    }

    @Override
    public Subscription addResponseObserver(@NotNull Action1<Response> subscriber) {
        return subject.subscribe(subscriber);
    }

}
