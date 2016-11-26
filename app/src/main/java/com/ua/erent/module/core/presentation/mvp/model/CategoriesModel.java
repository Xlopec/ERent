package com.ua.erent.module.core.presentation.mvp.model;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.support.annotation.Px;

import com.ua.erent.R;
import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.account.auth.user.domain.IUserAppService;
import com.ua.erent.module.core.account.auth.user.domain.bo.User;
import com.ua.erent.module.core.item.domain.ICategoryAppService;
import com.ua.erent.module.core.item.domain.bo.Category;
import com.ua.erent.module.core.networking.util.ConnectionManager;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.ICategoriesModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
import com.ua.erent.module.core.presentation.mvp.view.CategoriesActivity;
import com.ua.erent.module.core.presentation.mvp.view.InitialScreenActivity;
import com.ua.erent.module.core.presentation.mvp.view.util.IParcelableFutureBitmap;
import com.ua.erent.module.core.presentation.mvp.view.util.ImageUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Максим on 11/12/2016.
 */

public final class CategoriesModel implements ICategoriesModel {

    private final ICategoryAppService categoryAppService;
    private final ConnectionManager connectionManager;
    private final Application context;
    private final IAuthAppService authAppService;
    private final IUserAppService userAppService;

    private static class BitmapHolder implements IParcelableFutureBitmap {

        private final int resourceId;
        private Bitmap bitmap;

        public static final Creator<BitmapHolder> CREATOR = new Creator<BitmapHolder>() {
            @Override
            public BitmapHolder createFromParcel(Parcel in) {
                return new BitmapHolder(in);
            }

            @Override
            public BitmapHolder[] newArray(int size) {
                return new BitmapHolder[size];
            }
        };

        BitmapHolder(int resourceId) {
            this.resourceId = resourceId;
        }

        private BitmapHolder(Parcel in) {
            resourceId = in.readInt();
        }

        @NotNull
        @Override
        public Observable<Bitmap> fetch(@Px int width, @Px int height, @NotNull Context context) {

            return bitmap == null ? Observable.defer(() ->
                    Observable.create((Observable.OnSubscribe<Bitmap>) subscriber -> {

                        subscriber.onStart();

                        try {
                            bitmap = ImageUtils.decodeSampledBitmapFromResource(
                                    context.getResources(),
                                    R.drawable.bike_test,
                                    ImageUtils.pxToDp(width),
                                    ImageUtils.pxToDp(height));
                            subscriber.onNext(bitmap);
                        } finally {
                            subscriber.onCompleted();
                        }
                    }))
                    .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                    : Observable.just(bitmap);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            bitmap = null;
            dest.writeInt(resourceId);
        }
    }

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
    public Intent createLoginIntent() {
        return new Intent(context, InitialScreenActivity.class);
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
        return toModel(categoryAppService.getCachedCategories());
    }

    @Override
    public Observable<Collection<CategoryModel>> getOnCategoriesDeletedObs() {
        return categoryAppService.getOnCategoriesDeletedObs().map(this::toModel);
    }

    @Override
    public Observable<Collection<CategoryModel>> fetchCategories() {
        return categoryAppService.fetchCategories().map(this::toModel)
                .onErrorResumeNext(throwable ->
                        Observable.error(new Throwable(context.getString(R.string.categories_fetch_err))));
    }

    @Override
    public Observable<Collection<CategoryModel>> getOnCategoriesAddedObs() {
        return categoryAppService.getOnCategoriesAddedObs().map(this::toModel);
    }

    private Collection<CategoryModel> toModel(Collection<Category> categories) {

        final Collection<CategoryModel> result = new ArrayList<>(categories.size());

        for (final Category category : categories) {
            result.add(new CategoryModel(category.getId().getId(), category.getTitle(),
                    category.getDescription(), new BitmapHolder(R.drawable.bike_test)));
        }

        return result;
    }

}
