package com.ua.erent.module.core.item.domain.storage;

import com.ua.erent.module.core.item.domain.bo.Category;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by Максим on 11/12/2016.
 */

public interface ICategoriesStorage {

    void store(@NotNull Collection<Category> categories);

    void clear();

    Collection<Category> getAll();

}
