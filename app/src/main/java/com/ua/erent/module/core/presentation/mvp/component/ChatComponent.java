package com.ua.erent.module.core.presentation.mvp.component;

import com.ua.erent.module.core.app.AppComponent;
import com.ua.erent.module.core.di.scopes.ActivityScope;
import com.ua.erent.module.core.presentation.mvp.core.IMVPComponent;
import com.ua.erent.module.core.presentation.mvp.module.ChatModule;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IChatPresenter;
import com.ua.erent.module.core.presentation.mvp.view.ChatActivity;

import dagger.Component;

/**
 * Created by Максим on 12/5/2016.
 */
@Component(dependencies = AppComponent.class, modules = ChatModule.class)
@ActivityScope
public interface ChatComponent extends IMVPComponent<ChatActivity, IChatPresenter> {
}
