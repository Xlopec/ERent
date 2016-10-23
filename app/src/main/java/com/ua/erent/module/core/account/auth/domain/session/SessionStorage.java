package com.ua.erent.module.core.account.auth.domain.session;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.app.Constant;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import rx.subjects.PublishSubject;

public final class SessionStorage implements ISessionStorage {

    private static final String ARG_ACC_KEY = "argAccKey";
    private static final String ARG_STORAGE = "argSessionStorage";
    private final AccountManager accountManager;
    private final SharedPreferences preferences;
    private Session currentSession;
    private final PublishSubject<Session> sessionObservable;

    @Inject
    public SessionStorage(Context context) {

        sessionObservable = PublishSubject.create();
        accountManager = AccountManager.get(context.getApplicationContext());
        preferences = context.getSharedPreferences(SessionStorage.ARG_STORAGE, Context.MODE_PRIVATE);

        // get last used account name
        final String lastAccount = preferences.getString(SessionStorage.ARG_ACC_KEY, null);
        // restore session if application was destroyed by the system, not by user
        if (lastAccount != null) {

            final Account account = getAccountByName(lastAccount);

            if (account != null) {

                final String authToken = accountManager.peekAuthToken(account, Constant.ACCOUNT_TYPE);

                if (authToken != null) {
                    // session can be set
                    final Session newSession = new Session(account.name, authToken, account.type);

                    currentSession = newSession;
                    sessionObservable.onNext(newSession);
                }
            }
        }
    }

    @Override
    public void store(@NotNull Session session) {

        Account account;

        synchronized (this) {

            if ((account = getAccountByName(session.getLogin())) == null) {
                // account doesn't exist, create a new one
                account = new Account(session.getLogin(), Constant.ACCOUNT_TYPE);
                accountManager.addAccountExplicitly(account, null, null);
            }
            // reset auth token for a given account
            accountManager.setAuthToken(account, Constant.ACCOUNT_TOKEN_TYPE, session.getToken());
            preferences.edit().putString(SessionStorage.ARG_ACC_KEY, session.getLogin()).apply();
            sessionObservable.onNext(session);
        }
    }

    @Override
    public void clear() {

        final Session session = getSession();

        if(session != null) {
            accountManager.invalidateAuthToken(Constant.ACCOUNT_TYPE, session.getToken());
            preferences.edit().putString(SessionStorage.ARG_ACC_KEY, null).apply();
        }
    }

    @Override
    public boolean hasSession() {
        return getSession() != null;
    }

    @Override
    public Session getSession() {
        return currentSession;
    }

    @Override
    public rx.Observable<Session> getSessionObs() {
        return sessionObservable.asObservable();
    }

    private Account getAccountByName(@NotNull String name) {

        final Account[] accounts = accountManager.getAccountsByType(Constant.ACCOUNT_TYPE);

        for (final Account account : accounts) {
            if (account.name.equals(name)) {
                return account;
            }
        }

        return null;
    }

}
