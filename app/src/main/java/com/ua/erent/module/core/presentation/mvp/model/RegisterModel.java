package com.ua.erent.module.core.presentation.mvp.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.ua.erent.R;
import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.exception.FileUploadException;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.IRegisterModel;
import com.ua.erent.module.core.presentation.mvp.util.SignUpFormConverter;
import com.ua.erent.module.core.presentation.mvp.view.CategoriesActivity;

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
    public Intent createLoginIntent(@NotNull Context context) {

        final Intent intent = new Intent(context, CategoriesActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return intent;
    }

    @Override
    public Observable<Void> signUp(@NotNull SignUpForm form) {
        return authAppService.signUp(SignUpFormConverter.convert(context, form))
                .onErrorResumeNext(throwable -> {

                    if(throwable instanceof FileUploadException) {
                        Observable.error(new Throwable(context.getString(R.string.register_upload_avatar_failure)));
                    }

                    return Observable.error(new Throwable(context.getString(R.string.register_operation_failure)));
                });
    }

    @Override
    public Observable<Bitmap> resizeBitmap(@NotNull Bitmap original, int h, int w) {
        return Observable.just(Bitmap.createScaledBitmap(original, w, h, true))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
