package com.ua.erent.module.core.account.auth.user.domain;

import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.account.auth.user.api.IUserProvider;
import com.ua.erent.module.core.account.auth.user.domain.bo.User;
import com.ua.erent.module.core.account.auth.user.domain.vo.ContactInfo;
import com.ua.erent.module.core.account.auth.user.domain.vo.FullName;
import com.ua.erent.module.core.account.auth.user.domain.vo.PasswordForm;
import com.ua.erent.module.core.account.auth.user.domain.vo.UserForm;
import com.ua.erent.module.core.app.domain.ComponentKind;
import com.ua.erent.module.core.app.domain.interfaces.IAppLifecycleManager;
import com.ua.erent.module.core.storage.ISingleItemStorage;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.internal.Preconditions;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
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
    public Observable<User> fetchUserProfile() {

        if (!authService.isSessionAlive())
            throw new IllegalStateException(String.format("%s session expired", getClass()));

        return fetchUserProfile(authService.getSession());
    }

    @Override
    public Observable<User> updateUserProfile(@NotNull UserForm userForm) {

        Preconditions.checkNotNull(userForm, "update form == null");

        if (!authService.isSessionAlive())
            throw new IllegalStateException(String.format("%s session expired", getClass()));

        return userProvider.updateUserProfile(authService.getSession(), userForm)
                .flatMap(v -> {
                    User updated = null;

                    synchronized (storage) {
                        final User user = storage.getItem();

                        if (user != null) {
                            // updates user by applying update form
                            updated = new User.Builder(user)
                                    .setContactInfo(new ContactInfo(userForm.getEmail()))
                                    .setFullName(new FullName(userForm.getUsername()))
                                    .build();

                            if (!user.equals(updated)) {
                                storage.store(updated);
                                userChangedPublisher.onNext(updated);
                            }
                        }
                    }
                    return Observable.just(updated);
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Void> uploadAvatar(@NotNull File avatar) {

        Preconditions.checkNotNull(avatar);

        if (!avatar.exists() || !avatar.isFile())
            throw new IllegalArgumentException(String.format("Invalid user avatar file exists %s, is file %s",
                    avatar.exists(), avatar.isFile()));

        if (!authService.isSessionAlive())
            throw new IllegalStateException(String.format("%s session expired", getClass()));

        return userProvider.uploadAvatar(authService.getSession(), avatar);
    }

    @Override
    public User getCachedProfile() {
        return storage.getItem();
    }

    @Override
    public Observable<User> getOnUserProfileChangedObservable() {
        return userChangedPublisher.observeOn(AndroidSchedulers.mainThread()).asObservable();
    }

    @Override
    public Observable<Void> changePassword(@NotNull PasswordForm form) {

        if (!authService.isSessionAlive())
            throw new IllegalStateException(String.format("%s session expired", getClass()));

        Preconditions.checkNotNull(form);
        return userProvider.changePassword(authService.getSession(), form);
    }

    @Override
    public Observable<Initializeable> initialize(@NotNull Session session) {
        // load user profile
        return fetchUserProfile(session).flatMap(val -> Observable.just(UserDomain.this));
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

    private Observable<User> fetchUserProfile(Session session) {

        return userProvider.fetchUserProfile(session)
                .flatMap(user -> {

                    if (!user.getId().equals(session.getUserId()))
                        // user which was logged and which loading profile
                        // should have be same ids
                        throw new IllegalArgumentException(
                                String.format("invalid user id, session user id %s, actual %s",
                                        user.getId(), session.getUserId()));

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
                    return Observable.just(user);
                }).observeOn(AndroidSchedulers.mainThread());
    }

    private void clearCaches() {
        storage.clear();
    }
}
