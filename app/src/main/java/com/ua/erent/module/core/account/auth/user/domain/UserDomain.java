package com.ua.erent.module.core.account.auth.user.domain;

import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.account.auth.user.api.IUserProvider;
import com.ua.erent.module.core.account.auth.domain.vo.Profile;
import com.ua.erent.module.core.account.auth.domain.vo.UserID;
import com.ua.erent.module.core.account.auth.user.domain.bo.User;
import com.ua.erent.module.core.app.domain.ComponentKind;
import com.ua.erent.module.core.app.domain.interfaces.IAppLifecycleManager;
import com.ua.erent.module.core.storage.ISingleItemStorage;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Максим on 11/5/2016.
 */
@Singleton
public final class UserDomain implements IUserDomain, IAppLifecycleManager.IStateCallback {

    private final IUserProvider userProvider;
    private final IAuthAppService authService;
    private final ISingleItemStorage<User> storage;
    private final PublishSubject<User> userChangedPublisher;

    @Inject
    public UserDomain(IUserProvider userProvider, IAuthAppService authService,
                      IAppLifecycleManager lifecycleManager, ISingleItemStorage<User> storage) {
        this.userProvider = userProvider;
        this.authService = authService;
        this.userChangedPublisher = PublishSubject.create();
        this.storage = storage;
        authService.getSessionObs().filter(Session::isExpired).subscribe(session -> clearCaches());
        lifecycleManager.registerCallback(ComponentKind.APP_SERVICE, this);
    }

    @Override
    public Observable<User> fetchUserProfile(@NotNull UserID id) {

        if (!authService.isSessionAlive())
            throw new IllegalStateException("session expired");

        return fetchUserProfile(id, authService.getSession());
    }

    @Override
    public Observable<User> updateUserProfile(@NotNull Profile userProfile) {
        return null;
    }

    @Override
    public User getCachedProfile() {
        return storage.getItem();
    }

    @Override
    public Observable<User> getOnUserProfileChangedObservable() {
        return userChangedPublisher.asObservable();
    }

    @Override
    public Observable<Initializeable> initialize(@NotNull Session session) {
        // load user profile
        return fetchUserProfile(session.getUserId(), session)
                .flatMap(val -> Observable.just(UserDomain.this));
    }

    @Override
    public void onReject() {
        // clear all caches
        clearCaches();
    }

    @Override
    public boolean failOnException() {
        return true;
    }

    @Override
    public void onRestoreState() {
        // nothing
    }

    @Override
    public void onSaveState() {
        // nothing
    }

    private Observable<User> fetchUserProfile(UserID id, Session session) {

        return userProvider.fetchUserProfile(session, id)
                .flatMap(user -> {
                    // user which was logged in and loading profile
                    // are same ones
                    if (user.getId().equals(session.getUserId())) {

                        User currentUser = null;

                        if (storage.hasItem()) {
                            currentUser = storage.getItem();
                        }

                        if (currentUser == null || !currentUser.equals(user)) {
                            // user data was updated or not set
                            // update storage
                            storage.store(user);
                            userChangedPublisher.onNext(currentUser);
                        }
                    }
                    return Observable.just(user);
                });
    }

    private void clearCaches() {
        storage.clear();
    }
}
