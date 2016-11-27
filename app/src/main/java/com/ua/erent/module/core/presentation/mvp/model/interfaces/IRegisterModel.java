package com.ua.erent.module.core.presentation.mvp.model.interfaces;

import com.ua.erent.module.core.presentation.mvp.model.SignUpForm;

import org.jetbrains.annotations.NotNull;

import rx.Observable;

/**
 * Created by Максим on 10/27/2016.
 */

public interface IRegisterModel {

    Observable<Void> signUp(@NotNull SignUpForm form);

}
