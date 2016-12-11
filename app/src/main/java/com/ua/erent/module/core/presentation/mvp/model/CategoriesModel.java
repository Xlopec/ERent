package com.ua.erent.module.core.presentation.mvp.model;

import android.app.Application;
import android.content.Intent;

import com.ua.erent.R;
import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.account.auth.user.domain.IUserAppService;
import com.ua.erent.module.core.account.auth.user.domain.bo.User;
import com.ua.erent.module.core.item.domain.ICategoryAppService;
import com.ua.erent.module.core.networking.util.ConnectionManager;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.ICategoriesModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IItemsPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
import com.ua.erent.module.core.presentation.mvp.util.ItemConverter;
import com.ua.erent.module.core.presentation.mvp.view.CategoriesActivity;
import com.ua.erent.module.core.presentation.mvp.view.InitialScreenActivity;
import com.ua.erent.module.core.presentation.mvp.view.ItemsActivity;
import com.ua.erent.module.core.presentation.mvp.view.MyItemsActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Максим on 11/12/2016.
 */

public final class CategoriesModel implements ICategoriesModel {

    private final ICategoryAppService categoryAppService;
    private final ConnectionManager connectionManager;
    private final Application context;
    private final IAuthAppService authAppService;
    private final IUserAppService userAppService;

    @Inject
    public CategoriesModel(Application context, ICategoryAppService categoryAppService,
                           ConnectionManager connectionManager,
                           IAuthAppService authAppService, IUserAppService userAppService) {
        this.categoryAppService = categoryAppService;
        this.context = context;
        this.connectionManager = connectionManager;
        this.authAppService = authAppService;
        this.userAppService = userAppService;
    }

    @Override
    public void logout() {
        authAppService.logout();
    }

    @Override
    public Intent createMyItemsIntent() {
        return new Intent(context, MyItemsActivity.class);
    }

    @Override
    public Intent createLoginIntent() {
        return new Intent(context, InitialScreenActivity.class);
    }

    @Override
    public Intent createItemsIntent(@NotNull CategoryModel category) {

        final Intent intent = new Intent(context, ItemsActivity.class);

        intent.putExtra(IItemsPresenter.ARG_CATEGORY, category);
        return intent;
    }

    @Override
    public Intent createLogoutIntent() {

        final Intent intent = new Intent(context, CategoriesActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return intent;
    }

    @Override
    public Observable<User> fetchUserProfile() {
        return userAppService.fetchUserProfile();
    }

    @Override
    public User getCachedProfile() {
        return userAppService.getCachedProfile();
    }

    @Override
    public Observable<User> getOnUserProfileChangedObservable() {
        return userAppService.getOnUserProfileChangedObservable();
    }

    @Override
    public Observable<Session> getSessionObs() {
        return authAppService.getSessionObs();
    }

    @Override
    public boolean isSessionAlive() {
        return authAppService.isSessionAlive();
    }

    @Override
    public Session getSession() {
        return authAppService.getSession();
    }

    @Override
    public boolean hasConnection() {
        return connectionManager.hasConnection();
    }

    @Override
    public Observable<Boolean> getNetworkObservable() {
        return connectionManager.getNetworkObservable();
    }

    @Override
    public Collection<CategoryModel> getCategories() {
        return ItemConverter.toCategoryModel(categoryAppService.getCachedCategories());
    }

    @Override
    public Observable<Collection<CategoryModel>> getOnCategoriesDeletedObs() {
        return categoryAppService.getOnCategoriesDeletedObs().map(ItemConverter::toCategoryModel);
    }

    @Override
    public Observable<Collection<CategoryModel>> fetchCategories() {
        return categoryAppService.fetchCategories().map(ItemConverter::toCategoryModel)
                .onErrorResumeNext(throwable ->
                        Observable.error(new Throwable(context.getString(R.string.categories_fetch_err))));
    }

    @Override
    public Observable<Collection<CategoryModel>> getOnCategoriesAddedObs() {
        return categoryAppService.getOnCategoriesAddedObs().map(ItemConverter::toCategoryModel);
    }

}
