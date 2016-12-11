package com.ua.erent.module.core.presentation.mvp.view.interfaces;

import com.ua.erent.module.core.presentation.mvp.core.IBaseView;
import com.ua.erent.module.core.presentation.mvp.view.util.UriBitmap;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 12/9/2016.
 */

public interface ItemCreationView extends IBaseView {

    class SelectableModel {
        public final long id;
        public final String title;

        public SelectableModel(long id, String title) {
            this.id = id;
            this.title = title;
        }
    }

    void clearImages();

    void showMessage(String string);

    void showProgress(@NotNull String message);

    void hideProgress();

    void addImage(@NotNull UriBitmap bitmap);

}
