package com.ua.erent.module.core.account.auth.domain.session;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.table.TableUtils;
import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.domain.api.db.SessionDao;
import com.ua.erent.module.core.account.auth.domain.api.db.SessionMapper;
import com.ua.erent.module.core.account.auth.domain.api.db.SessionPO;
import com.ua.erent.module.core.app.Constant;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import rx.subjects.PublishSubject;

public final class SessionStorage implements ISessionStorage {

    private static final String TAG = SessionStorage.class.getSimpleName();

    private final Context context;
    private final SessionDao dao;
    private final AccountManager accountManager;
    private final PublishSubject<Session> sessionObservable;
    private Session currentSession;

    @Inject
    public SessionStorage(Context context, SessionDao dao) {

        this.dao = Preconditions.checkNotNull(dao, "session dao was null");
        this.context = context;
        final CloseableIterator<SessionPO> it = this.dao.iterator();

        sessionObservable = PublishSubject.create();
        accountManager = AccountManager.get(context.getApplicationContext());
        SessionPO po = null;

        do {
            // while database has stored session po, move cursor
            if (it.hasNext()) {

                po = it.next();

                final String token = accountManager
                        .peekAuthToken(getAccountByName(po.getLogin()), Constant.ACCOUNT_TOKEN_TYPE);
                // peek auth token, that was cached by account manager
                if (token != null) {
                    // token is still valid, session can be set
                    currentSession = SessionMapper.toSession(po);
                } else {
                    // session token is invalid, clear table and continue
                    try {
                        TableUtils.clearTable(dao.getConnectionSource(), SessionPO.class);
                    } catch (final SQLException e) {
                        Log.e(TAG, "exception while clearing table", e);
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
        } while (po != null && it.hasNext());

        it.closeQuietly();
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

            try {
                dao.createOrUpdate(SessionMapper.toPersistenceObject(session));
            } catch (final SQLException e) {
                Log.e(TAG, "exception while updating session table", e);
                throw new RuntimeException(e);
            }
            // reset auth token for a given account
            accountManager.setAuthToken(account, Constant.ACCOUNT_TOKEN_TYPE, session.getToken());
            sessionObservable.onNext(session);
        }
    }

    @Override
    public void clear() {

        final Session session = getSession();

        if (session != null) {

            accountManager.invalidateAuthToken(Constant.ACCOUNT_TYPE, session.getToken());

            try {
                TableUtils.clearTable(dao.getConnectionSource(), SessionPO.class);
            } catch (final SQLException e) {
                Log.e(TAG, "exception while clearing table", e);
                throw new RuntimeException(e);
            }
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

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED)
            throw new RuntimeException("Permission to get accounts not granted");

        final Account[] accounts = accountManager.getAccountsByType(Constant.ACCOUNT_TYPE);

        for (final Account account : accounts) {
            if (account.name.equals(name)) {
                return account;
            }
        }

        return null;
    }

}
