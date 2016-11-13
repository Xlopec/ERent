package com.ua.erent.module.core.presentation.mvp.view;

import android.content.Context;

import com.ua.erent.R;
import com.ua.erent.module.core.di.target.InjectableActivity;
import com.ua.erent.module.core.presentation.mvp.component.ItemsComponent;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IItemsPresenter;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.IItemsView;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 11/13/2016.
 */

public final class ItemActivity extends InjectableActivity<ItemActivity, IItemsPresenter>
        implements IItemsView {

    public ItemActivity() {
        super(R.layout.activity_items, ItemsComponent.class);
    }

    @NotNull
    @Override
    public Context getContext() {
        return this;
    }
}
