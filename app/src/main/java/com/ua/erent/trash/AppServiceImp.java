package com.ua.erent.trash;

import android.content.Context;
import android.widget.Toast;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Максим on 10/11/2016.
 */
public class AppServiceImp implements IAppService {

    static int i = 0;

    private final Context context;

    @Inject
    public AppServiceImp(Context context) {
        this.context = context;
      //  Toast.makeText(context, String.format("%d !!!", i), Toast.LENGTH_SHORT).show();
        i++;
    }

    @Override
    public void z() {
        Toast.makeText(context, "z() was fired", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Observable<Initializeable> initialize(@NotNull Session session) {
        return Observable.timer(5000L, TimeUnit.MILLISECONDS, Schedulers.newThread()).map(l -> AppServiceImp.this);//just(this);
    }

    @Override
    public void onReject() {

    }

    @Override
    public boolean failOnException() {
        return false;
    }

}
