package com.ua.erent.trash;

import android.content.Context;
import android.widget.Toast;

import com.ua.erent.module.core.account.auth.bo.Session;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

/**
 * Created by Максим on 10/11/2016.
 */
public class AppServiceImp implements IAppService {

    static int i = 0;

    private final Context context;

    @Inject
    public AppServiceImp(Context context) {
        this.context = context;
        Toast.makeText(context, String.format("%d !!!", i), Toast.LENGTH_SHORT).show();
        i++;
    }

    @Override
    public void z() {
        Toast.makeText(context, "z() was fired", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initialize(@NotNull Session session, @NotNull ICallback callback) {
        callback.onInitialized();
    }

    @Override
    public void reject() {

    }

    @Override
    public boolean failOnException() {
        return false;
    }
}
