package com.ua.erent.trash;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.app.domain.ComponentKind;
import com.ua.erent.module.core.app.domain.interfaces.IAppLifecycleManager;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Максим on 10/11/2016.
 */
public class SomeAppServiceImp implements ISomeAppService, IAppLifecycleManager.IStateCallback {

    static int i = 0;

    private final Context context;

    @Inject
    public SomeAppServiceImp(Context context, IAppLifecycleManager lifecycleManager) {
        this.context = context;
        lifecycleManager.registerCallback(ComponentKind.APP_SERVICE, this);
      //  Toast.makeText(context, String.format("%d !!!", i), Toast.LENGTH_SHORT).show();
        i++;
    }

    @Override
    public void z() {
        Toast.makeText(context, "z() was fired", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Observable<Initializeable> initialize(@NotNull Session session) {
        return Observable.timer(5000L, TimeUnit.MILLISECONDS, Schedulers.newThread()).map(l -> SomeAppServiceImp.this);//just(this);
    }

    @Override
    public void onReject() {

    }

    @Override
    public boolean failOnException() {
        return false;
    }

    @Override
    public void onRestoreState() {
        Log.i("tag", "#onRestoreState");
    }

    @Override
    public void onSaveState() {
        Log.i("tag", "#onSaveState");
    }
}
