package com.ua.erent.module.core.presentation.mvp.presenter;

import android.os.Bundle;

import com.ua.erent.module.core.presentation.mvp.model.interfaces.IChatModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IChatPresenter;
import com.ua.erent.module.core.presentation.mvp.view.ChatActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Максим on 12/7/2016.
 */

public final class ChatPresenter extends IChatPresenter {
    public ChatPresenter(IChatModel model) {

    }

    @Override
    protected void onViewAttached(@NotNull ChatActivity view, @Nullable Bundle savedState, @Nullable Bundle data) {

    }

    @Override
    protected void onDestroyed() {

    }
}
