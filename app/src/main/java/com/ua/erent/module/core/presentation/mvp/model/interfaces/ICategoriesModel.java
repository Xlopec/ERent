package com.ua.erent.module.core.presentation.mvp.model.interfaces;

import android.content.Intent;

import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.account.auth.user.domain.bo.User;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import rx.Observable;

/**
 * Created by Максим on 11/12/2016.
 */

public interface ICategoriesModel {

    void logout();

    Intent createLoginIntent();

    Intent createItemsIntent(@NotNull CategoryModel category);

    Intent createLogoutIntent();

    Observable<User> fetchUserProfile();

    User getCachedProfile();

    Observable<User> getOnUserProfileChangedObservable();

    Observable<Session> getSessionObs();

    boolean isSessionAlive();

    Session getSession();

    boolean hasConnection();

    Observable<Boolean> getNetworkObservable();

    Collection<CategoryModel> getCategories();

    Observable<Collection<CategoryModel>> getOnCategoriesDeletedObs();

    Observable<Collection<CategoryModel>> fetchCategories();

    Observable<Collection<CategoryModel>> getOnCategoriesAddedObs();
}
