package com.ua.erent.module.core.account.auth.domain;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.domain.api.ISessionProvider;
import com.ua.erent.module.core.account.auth.domain.init.InitializationManager;
import com.ua.erent.module.core.account.auth.domain.session.ISessionManager;
import com.ua.erent.module.core.account.auth.vo.Credentials;
import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Максим on 10/15/2016.
 */

public final class AuthDomain implements IAuthDomain {

    private final Application application;
    private final ISessionManager sessionManager;
    private final ISessionProvider provider;
    private final InitializationManager initializationManager;
    private final Class<? extends Activity> loginActivity;
    private final Collection<? extends Initializeable> initializeables;

    private final class LoginCallbackWrapper implements ILoginCallback {

        private final ILoginCallback original;
        private final Session session;

        LoginCallbackWrapper(ILoginCallback original, Session session) {
            this.original = original;
            this.session = session;
        }

        @Override
        public void onPreExecute() {
            original.onPreExecute();
        }

        @Override
        public void onInitialized() {
            sessionManager.setSession(session);
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
                      ISessionManager sessionManager, ISessionProvider provider,
                      InitializationManager initializationManager,
                      Collection<? extends Initializeable> initializeables) {

        this.loginActivity = loginActivity;
        this.application = application;
        this.sessionManager = sessionManager;
        this.provider = provider;
        this.initializationManager = initializationManager;
        this.initializeables = Collections.unmodifiableCollection(initializeables);
    }

    @Override
    public void login(@NotNull Credentials credentials, @NotNull ILoginCallback callback) {
        /*
         * At first, try to get a session from server,
         * then try to initialize modules which needs it;
         * After this operations session can be finally set
         */
        provider.fetchSession(credentials).subscribe(new Subscriber<Session>() {

            @Override
            public void onCompleted() { unsubscribe(); }

            @Override
            public void onError(Throwable e) {
                unsubscribe();
                callback.onFailure(e);
            }

            @Override
            public void onNext(Session session) {
                unsubscribe();
                initializationManager.
                        initialize(session, initializeables, new LoginCallbackWrapper(callback, session));
            }
        });
    }

    @Override
    public void logout() {
        // destroy session, activities and tasks and open login activity
        final Session session = sessionManager.getSession();
        final Intent intent = new Intent(application, loginActivity);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, session.getLogin());

        sessionManager.destroySession();
        application.startActivity(intent);
    }

}
