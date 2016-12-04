package com.ua.erent.module.core.presentation.mvp.view.interfaces;

import com.ua.erent.module.core.presentation.mvp.core.IBaseView;
import com.ua.erent.module.core.presentation.mvp.view.util.IFutureBitmap;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 12/3/2016.
 */

public interface ItemDetailsView extends IBaseView {

    void setTitle(@NotNull String title);

    void setGalleryPreviewImage(@NotNull IFutureBitmap bitmap);

    void setMainInfo(@NotNull String mainInfo);

    void setDescription(@NotNull String description);

    void setPubDate(@NotNull String pubDate);

    void setUsername(@NotNull String username);

    void setAvatar(@NotNull IFutureBitmap bitmap);

    void showText(@NotNull String text);

}
