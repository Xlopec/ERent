package com.ua.erent.module.core.account.auth.domain.session;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ReferenceObjectCache;
import com.j256.ormlite.table.TableUtils;
import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.storage.DatabaseHelper;
import com.ua.erent.module.core.account.auth.domain.session.storage.SessionMapper;
import com.ua.erent.module.core.account.auth.domain.session.storage.SessionPO;
import com.ua.erent.module.core.app.Constant;
import com.ua.erent.module.core.storage.ISingleItemStorage;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

import javax.inject.Inject;

public final class SessionStorage implements ISingleItemStorage<Session> {

    private static final String TAG = SessionStorage.class.getSimpleName();

    private final AccountManager accountManager;
    private final DatabaseHelper helper;
    private Session cachedSession;
    private boolean cleaned;

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
                createDao().createOrUpdate(SessionMapper.toPersistenceObject(session));
                cachedSession = session;
                cleaned = false;
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
            cachedSession = null;
            helper.clear(SessionPO.class);
        }
        cleaned = true;
    }

    @Override
    public Session getItem() {

        if(cachedSession == null && !cleaned) {
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
            final BaseDaoImpl<SessionPO, Integer> dao = helper.getDao(SessionPO.class);//new SessionDao(helper.getConnectionSource(), SessionPO.class);
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

                final String token = accountManager
                        .peekAuthToken(getAccountByName(po.getLogin()), Constant.ACCOUNT_TOKEN_TYPE);
                // peek auth token, that was cached by account manager
                if (token != null) {
                    // token is still valid, session can be set
                    session = SessionMapper.toSession(po);
                } else {
                    // session token is invalid, clear table and continue
                    try {
                        TableUtils.clearTable(helper.getConnectionSource(), SessionPO.class);
                    } catch (final SQLException e) {
                        Log.e(TAG, "exception while clearing table", e);
                        helper.close();
                        throw new RuntimeException(e);
                    }
                    break;
                }
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
