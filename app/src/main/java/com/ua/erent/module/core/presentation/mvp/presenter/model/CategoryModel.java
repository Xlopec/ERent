package com.ua.erent.module.core.presentation.mvp.presenter.model;

import com.ua.erent.module.core.presentation.mvp.view.util.IFutureBitmap;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 11/12/2016.
 */

public final class CategoryModel {

    private final int id;
    private final String title;
    private final String description;
    private final IFutureBitmap futureBitmap;

    public CategoryModel(int id, String title, String description, IFutureBitmap futureBitmap) {
        this.id = id;
        this.title = Preconditions.checkNotNull(title);
        this.description = Preconditions.checkNotNull(description);
        this.futureBitmap = futureBitmap;
    }

    public IFutureBitmap getFutureBitmap() {
        return futureBitmap;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
