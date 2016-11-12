package com.ua.erent.module.core.item.domain.storage;

import com.ua.erent.module.core.item.domain.bo.Category;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by Максим on 11/13/2016.
 */

public final class CategoryStorage implements ICategoriesStorage {
    @Override
    public void store(@NotNull Collection<Category> categories) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Collection<Category> getAll() {
        return null;
    }
}
