package com.ua.erent.module.core.presentation.mvp.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.IRegisterModel;
import com.ua.erent.module.core.presentation.mvp.util.SignUpFormConverter;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Максим on 10/27/2016.
 */

public final class RegisterModel implements IRegisterModel {

    private final IAuthAppService authAppService;
    private final Context context;

    @Inject
    public RegisterModel(Context context, IAuthAppService authAppService) {
        this.context = context;
        this.authAppService = authAppService;
    }

    @Override
    public Observable<Void> signUp(@NotNull SignUpForm form) {
        return authAppService.signUp(SignUpFormConverter.convert(context, form));
    }

    @Override
    public Observable<Bitmap> resizeBitmap(@NotNull Bitmap original, int h, int w) {
        return Observable.just(Bitmap.createScaledBitmap(original, w, h, true))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
