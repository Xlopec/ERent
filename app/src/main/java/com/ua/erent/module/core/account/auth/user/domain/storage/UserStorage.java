package com.ua.erent.module.core.account.auth.user.domain.storage;

import android.util.Log;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ReferenceObjectCache;
import com.ua.erent.module.core.account.auth.user.domain.User;
import com.ua.erent.module.core.storage.DatabaseHelper;
import com.ua.erent.module.core.storage.ISingleItemStorage;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

import javax.inject.Inject;

import static android.content.ContentValues.TAG;

/**
 * Created by Максим on 11/5/2016.
 */

public final class UserStorage implements ISingleItemStorage<User> {

    private final DatabaseHelper helper;

    private boolean cleared;
    private User cachedUser;

    @Inject
    public UserStorage(DatabaseHelper helper) {
        this.helper = helper;
    }

    @Override
    public void store(@NotNull User user) {

        try {
            createDao().createOrUpdate(UserMapper.toPersistenceObject(user));
            cleared = false;
        } catch (final SQLException e) {
            Log.e(TAG, "exception while updating user table", e);
            helper.close();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() {
        cachedUser = null;
        helper.clear(UserPO.class);
        cleared = true;
    }

    @Override
    public User getItem() {

        if(!hasItem() && !cleared) {
            cachedUser = getUser();
        }

        return cachedUser;
    }

    @Override
    public boolean hasItem() {
        return cachedUser != null;
    }

    private BaseDaoImpl<UserPO, Long> createDao() {

        try {
            final BaseDaoImpl<UserPO, Long> dao = helper.getDao(UserPO.class);//new SessionDao(helper.getConnectionSource(), SessionPO.class);
            dao.setObjectCache(true);
            dao.setObjectCache(ReferenceObjectCache.makeSoftCache());
            return dao;
        } catch (final SQLException e) {
            Log.e(TAG, "Exception while creating user dao", e);
            helper.close();
            throw new RuntimeException(e);
        }
    }

    private User getUser() {

        final BaseDaoImpl<UserPO, Long> dao = createDao();
        final CloseableIterator<UserPO> it = dao.iterator();

        UserPO po = null;
        User user = null;

        do {
            // while database has stored session po, move cursor
            if (it.hasNext()) {
                po = it.next();
                user = UserMapper.toUser(po);
            }
        } while (po != null && it.hasNext());

        it.closeQuietly();
        return user;
    }

}
