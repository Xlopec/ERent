package com.ua.erent.module.core.presentation.mvp.presenter;

import android.os.Bundle;
import android.util.Log;

import com.ua.erent.BuildConfig;
import com.ua.erent.R;
import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.account.auth.user.domain.bo.User;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.ICategoriesModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ICategoriesPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
import com.ua.erent.module.core.presentation.mvp.view.CategoriesActivity;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.ICategoriesView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import rx.Subscription;

/**
 * Created by Максим on 11/12/2016.
 */

public final class CategoriesPresenter extends ICategoriesPresenter {

    private static final String ARG_CACHED_CATEGORIES = "argCachedCategories";
    private static final String TAG = CategoriesPresenter.class.getSimpleName();

    private static final int[] CATEGORY_COLORS = {
            0xcc0000,// red
            0x333399,// blue
            0xff9900,//orange
            0x339933,//green
            0x00cccc//cyan
    };

    private final ICategoriesModel model;
    private final ArrayList<CategoryModel> localCache;
    private final Subscription userSubscriber;
    private final Subscription sessionSubscriber;

    @Inject
    public CategoriesPresenter(ICategoriesModel model) {
        this.model = model;
        this.localCache = new ArrayList<>(0);

        userSubscriber = model.getOnUserProfileChangedObservable()
                .subscribe(user -> {
                    if (!isViewGone()) {
                        syncWithView(user);
                    }
                });

        sessionSubscriber = model.getSessionObs()
                .subscribe(session -> {
                    if (!isViewGone()) {
                        syncWithView(session);
                    }
                });
    }

    @Override
    protected void onViewAttached(@NotNull CategoriesActivity view, @Nullable Bundle savedState, @Nullable Bundle data) {

        if (isFirstTimeAttached()) {
            setupDrawer();
            setupCategories(savedState);
        }
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        outState.putParcelableArrayList(CategoriesPresenter.ARG_CACHED_CATEGORIES, localCache);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroyed() {
        userSubscriber.unsubscribe();
        sessionSubscriber.unsubscribe();
    }

    @Override
    public int getRandomColor() {
        return CATEGORY_COLORS[new Random().nextInt(CATEGORY_COLORS.length)];
    }

    @Override
    public void onOpenCategory(long categoryId) {

        System.out.println(categoryId);

    }

    @Override
    public void onRefresh() {

        model.fetchCategories().subscribe(categories -> {
                    if (!isViewGone()) {
                        syncWithView(categories);
                        getView().hideRefreshProgress();
                    }
                }, th -> {
                    if (!isViewGone()) {
                        getView().hideRefreshProgress();
                    }
                    Log.w(TAG, "error occurred on refresh#", th);
                }
        );
    }

    @Override
    public void onLogin() {
        getView().startActivity(model.createLoginIntent());
    }

    @Override
    public void onLogout() {
        model.logout();
        getView().startActivity(model.createLogoutIntent());
    }

    private void setupCategories(Bundle savedState) {

        final Collection<CategoryModel> categoryModels = model.getCategories();

        if (savedState == null) {

            if (model.hasConnection()) {
                // no cached categories available,
                // pull them from the api server
                model.fetchCategories()
                        .doOnSubscribe(getView()::showRefreshProgress)
                        .doOnCompleted(getView()::hideRefreshProgress)
                        .subscribe(this::syncWithView,
                                th -> {
                                    if (!isViewGone()) {
                                        getView().showMessage(th.getMessage());
                                        syncWithView(categoryModels);
                                        getView().hideRefreshProgress();
                                    }
                                    Log.w(TAG, "error on fetch categories#", th);
                                });
            } else if (!categoryModels.isEmpty()) {
                syncWithView(categoryModels);
            } else {
                getView().showMessage(getView().getString(R.string.categories_no_internet));
            }
        } else {
            syncWithView(savedState.getParcelableArrayList(CategoriesPresenter.ARG_CACHED_CATEGORIES));
        }
    }

    private void setupDrawer() {

        if (isViewGone())
            throw new IllegalStateException();

        final User user = model.getCachedProfile();

        if (model.isSessionAlive() && ((user == null && model.hasConnection()) || user != null)) {

            if (user == null) {

                model.fetchUserProfile().subscribe(profile -> {
                    if (!isViewGone()) {
                        getView().setDrawerMenu(R.menu.categories_drawer_authorized);
                        getView().setCredentials(profile.getFullName().getUsername(), profile.getContactInfo().getEmail());
                    }
                }, th -> {
                    if (!isViewGone()) {
                        getView().setDrawerMenu(R.menu.categories_drawer_unauthorized);
                        getView().setCredentials(getView().getString(R.string.main_drawer_credentials_title_unauthorized),
                                getView().getString(R.string.main_drawer_credentials_sub_title_unauthorized, BuildConfig.VERSION_NAME));
                        getView().showMessage(th.getMessage());
                    }
                });
            } else {
                getView().setDrawerMenu(R.menu.categories_drawer_authorized);
                getView().setCredentials(user.getFullName().getUsername(), user.getContactInfo().getEmail());
            }
        } else {
            getView().setDrawerMenu(R.menu.categories_drawer_unauthorized);
            getView().setCredentials(getView().getString(R.string.main_drawer_credentials_title_unauthorized),
                    getView().getString(R.string.main_drawer_credentials_sub_title_unauthorized, BuildConfig.VERSION_NAME));
        }
    }

    private void syncWithView(Collection<CategoryModel> categoryModels) {

        if (isViewGone())
            throw new IllegalStateException("nothing to sync with");

        Preconditions.checkNotNull(categoryModels);

        final ICategoriesView view = getView();

        localCache.clear();
        localCache.addAll(categoryModels);
        view.clearCategories();
        view.addCategory(categoryModels);
    }

    private void syncWithView(User user) {

        if (isViewGone())
            throw new IllegalStateException("nothing to sync with");

        if (user == null) {
            getView().setCredentials(getView().getString(R.string.main_drawer_credentials_title_unauthorized),
                    getView().getString(R.string.main_drawer_credentials_sub_title_unauthorized, BuildConfig.VERSION_NAME));
        } else {
            getView().setCredentials(user.getFullName().getUsername(), user.getContactInfo().getEmail());
        }
    }

    private void syncWithView(Session session) {

        if (isViewGone())
            throw new IllegalStateException("nothing to sync with");

        if (session.isExpired()) {
            syncWithView((User) null);
        } else {
            setupDrawer();
        }
    }

}
