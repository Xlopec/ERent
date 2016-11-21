package com.ua.erent.module.core.account.auth.domain.session.storage;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ReferenceObjectCache;
import com.ua.erent.module.core.account.auth.domain.bo.Session;
import com.ua.erent.module.core.app.Constant;
import com.ua.erent.module.core.storage.DatabaseHelper;
import com.ua.erent.module.core.storage.ISingleItemStorage;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

import javax.inject.Inject;

import static com.ua.erent.module.core.account.auth.domain.session.storage.SessionMapper.toPersistenceObject;

public final class SessionStorage implements ISingleItemStorage<Session> {

    private static final String TAG = SessionStorage.class.getSimpleName();

    private final AccountManager accountManager;
    private final DatabaseHelper helper;
    private Session cachedSession;

    @Inject
    public SessionStorage(Context context, DatabaseHelper helper) {
        this.helper = helper;
        this.accountManager = AccountManager.get(context.getApplicationContext());
    }

    @Override
    public void store(@NotNull Session session) {

        Account account;

        synchronized (this) {

            if ((account = getAccountByName(session.getUsername())) == null) {
                // account doesn't exist, create a new one
                account = new Account(session.getUsername(), Constant.ACCOUNT_TYPE);
                accountManager.addAccountExplicitly(account, null, null);
            }

            try {
                createDao().createOrUpdate(toPersistenceObject(session));
                cachedSession = session;
            } catch (final SQLException e) {
                Log.e(TAG, "exception while updating session table", e);
                helper.close();
                throw new RuntimeException(e);
            }
            // reset auth token for a given account
            accountManager.setAuthToken(account, Constant.ACCOUNT_TOKEN_TYPE, session.getToken());
        }
    }

    @Override
    public void clear() {

        final Session session = getItem();

        if (session != null) {

            accountManager.invalidateAuthToken(Constant.ACCOUNT_TYPE, session.getToken());

            final Session newSession = new Session.Modifier(session).setToken(null).create();
            final SessionPO po = SessionMapper.toPersistenceObject(newSession);

            cachedSession = newSession;

            try {
                createDao().update(po);
            } catch (final SQLException e) {
                Log.e(TAG, "db exception", e);
            }
        }
    }

    @Override
    public Session getItem() {

        if (cachedSession == null) {
            cachedSession = getSession();
        }

        return cachedSession;
    }

    @Override
    public boolean hasItem() {
        return getItem() != null;
    }

    private BaseDaoImpl<SessionPO, Integer> createDao() {

        try {
            final BaseDaoImpl<SessionPO, Integer> dao = helper.getDao(SessionPO.class);
            dao.setObjectCache(true);
            dao.setObjectCache(ReferenceObjectCache.makeSoftCache());
            return dao;
        } catch (final SQLException e) {
            Log.e(TAG, "Exception while creating session dao", e);
            helper.close();
            throw new RuntimeException(e);
        }
    }

    private Session getSession() {

        final BaseDaoImpl<SessionPO, Integer> dao = createDao();
        final CloseableIterator<SessionPO> it = dao.iterator();

        SessionPO po = null;
        Session session = null;

        do {
            // while database has stored session po, move cursor
            if (it.hasNext()) {
                po = it.next();
                session = SessionMapper.toSession(po);
            }
        } while (po != null && it.hasNext());

        it.closeQuietly();
        return session;
    }

    private Account getAccountByName(@NotNull String name) {

       /* if (ActivityCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED)
            throw new RuntimeException("Permission to get accounts not granted");*/

        final Account[] accounts = accountManager.getAccountsByType(Constant.ACCOUNT_TYPE);

        for (final Account account : accounts) {
            if (account.name.equals(name)) {
                return account;
            }
        }

        return null;
    }

}
