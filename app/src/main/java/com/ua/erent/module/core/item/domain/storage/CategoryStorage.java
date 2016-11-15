package com.ua.erent.module.core.item.domain.storage;

import android.util.Log;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.ReferenceObjectCache;
import com.j256.ormlite.table.TableUtils;
import com.ua.erent.module.core.item.domain.bo.Category;
import com.ua.erent.module.core.storage.DatabaseHelper;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Максим on 11/13/2016.
 */

public final class CategoryStorage implements ICategoriesStorage {

    private static final String TAG = CategoryStorage.class.getSimpleName();

    private final DatabaseHelper helper;
    private final Collection<Category> cache;

    public CategoryStorage(DatabaseHelper helper) {
        this.helper = helper;
        this.cache = new ArrayList<>(0);
    }

    @Override
    public void store(@NotNull Collection<Category> categories) {
        try {
            final BaseDaoImpl<CategoryPO, Long> dao = createDao();

            TableUtils.clearTable(dao.getConnectionSource(), CategoryPO.class);
            dao.create(CategoryMapper.toPo(categories));
            cache.clear();
            cache.addAll(categories);
        } catch (final SQLException e) {
            Log.e(TAG, "exception while updating category table", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() {
        try {
            TableUtils.clearTable(createDao().getConnectionSource(), CategoryPO.class);
            cache.clear();
        } catch (final SQLException e) {
            Log.e(TAG, "exception while updating category table", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Category> getAll() {

        if (cache.isEmpty()) {
            try {
                cache.addAll(CategoryMapper.fromPo(createDao().queryForAll()));
            } catch (final SQLException e) {
                Log.e(TAG, "exception while querying category table", e);
            }
        }
        return Collections.unmodifiableCollection(cache);
    }

    private BaseDaoImpl<CategoryPO, Long> createDao() {

        try {
            final BaseDaoImpl<CategoryPO, Long> dao = helper.getDao(CategoryPO.class);
            dao.setObjectCache(true);
            dao.setObjectCache(ReferenceObjectCache.makeSoftCache());
            return dao;
        } catch (final SQLException e) {
            Log.e(TAG, "Exception while creating category dao", e);
            helper.close();
            throw new RuntimeException(e);
        }
    }

}
