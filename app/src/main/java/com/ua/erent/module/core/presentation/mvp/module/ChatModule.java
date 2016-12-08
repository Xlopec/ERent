package com.ua.erent.module.core.presentation.mvp.module;

import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.presentation.mvp.model.ChatModel;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.IChatModel;
import com.ua.erent.module.core.presentation.mvp.presenter.ChatPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IChatPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Максим on 12/7/2016.
 */
@Module
public final class ChatModule {

    @Provides
    @ActivityScope
    IChatPresenter providePresenter(IChatModel model) {
        return new ChatPresenter(model);
    }

    @Provides
    @ActivityScope
    IChatModel provideModel() {
        return new ChatModel();
    }

}
