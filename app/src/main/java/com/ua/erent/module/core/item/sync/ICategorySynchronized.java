package com.ua.erent.module.core.item.sync;

import com.ua.erent.module.core.item.domain.bo.Category;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by Максим on 11/15/2016.
 */

public interface ICategorySynchronized {

    void synchronize(@NotNull Collection<Category> categories);

}
