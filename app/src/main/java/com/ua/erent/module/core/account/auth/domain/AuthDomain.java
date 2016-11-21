package com.ua.erent.module.core.account.auth.domain;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.ua.erent.module.core.account.auth.domain.api.auth.IAuthProvider;
import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.account.auth.domain.vo.SignInCredentials;
import com.ua.erent.module.core.account.auth.domain.vo.SignUpCredentials;
import com.ua.erent.module.core.init.IInitCallback;
import com.ua.erent.module.core.init.domain.IInitAppService;
import com.ua.erent.module.core.storage.ISingleItemStorage;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.subjects.PublishSubject;

/**
 * Created by Максим on 10/15/2016.
 */
@Singleton
public final class AuthDomain implements IAuthDomain {

    private final Application application;
    private final ISingleItemStorage<Session> sessionStorage;
    private final IAuthProvider provider;
    private final IInitAppService initAppService;
    private final Class<? extends Activity> loginActivity;
    private final PublishSubject<Session> sessionPublishSubject;

    private final class InitCallbackWrapper implements IInitCallback {

        private final IInitCallback original;
        private final Session session;

        InitCallbackWrapper(IInitCallback original, Session session) {
            this.original = original;
            this.session = session;
        }

        @Override
        public void onPreExecute() {
            original.onPreExecute();
        }

        @Override
        public void onInitialized() {
            sessionStorage.store(session);
            sessionPublishSubject.onNext(session);
            original.onInitialized();
        }

        @Override
        public void onComponentInitialized(@NotNull Initializeable initializeable, int progress, int total) {
            original.onComponentInitialized(initializeable, progress, total);
        }

        @Override
        public void onException(@NotNull Initializeable initializeable, @NotNull Throwable th) {
            original.onException(initializeable, th);
        }

        @Override
        public void onFailure(@NotNull Initializeable initializeable, @NotNull Throwable th) {
            original.onFailure(initializeable, th);
        }

        @Override
        public void onFailure(@NotNull Throwable th) {
            original.onFailure(th);
        }
    }

    @Inject
    public AuthDomain(Class<? extends Activity> loginActivity, Application application,
                      ISingleItemStorage<Session> sessionStorage,
                      IAuthProvider provider,
                      IInitAppService initAppService) {

        this.loginActivity = loginActivity;
        this.application = application;
        this.sessionStorage = sessionStorage;
        this.provider = provider;
        this.initAppService = initAppService;
        this.sessionPublishSubject = PublishSubject.create();
    }

    @Override
    public void signIn(@NotNull SignInCredentials credentials,
                       @NotNull IInitCallback callback) {

        callback.onPreExecute();

        /*
         * At first, try to get a session from server,
         * then try to initialize modules which needs it;
         * After this operations session can be finally set
         */
        provider.signIn(credentials).subscribe(new Subscriber<Session>() {

            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                unsubscribe();
                callback.onFailure(e);
            }

            @Override
            public void onNext(Session session) {
                unsubscribe();
                initAppService.
                        initialize(session, new InitCallbackWrapper(callback, session));
            }
        });
    }

    @Override
    public void signIn(@NotNull IInitCallback callback) {

        final Session session = sessionStorage.getItem();

        if(session == null || session.isExpired())
            throw new IllegalStateException("Session is not valid anymore");

        callback.onPreExecute();

        initAppService.
                initialize(session, new InitCallbackWrapper(callback, session));
    }

    @Override
    public Observable<Void> signUp(@NotNull SignUpCredentials credentials) {
        return provider.signUp(credentials).map(session -> null);
    }

    @Override
    public void logout() {
        // destroy session, activities and tasks and open signIn activity
        final Session session = sessionStorage.getItem();
        final Intent intent = new Intent(application, loginActivity);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, session.getUsername());

        sessionStorage.clear();
        sessionPublishSubject.onNext(new Session.Modifier(session).setToken(null).create());
        application.startActivity(intent);
    }

    @Override
    public Observable<Session> getSessionChangedObservable() {
        return sessionPublishSubject.asObservable();
    }

}
