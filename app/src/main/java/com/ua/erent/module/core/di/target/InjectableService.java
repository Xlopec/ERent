package com.ua.erent.module.core.di.target;

import android.app.Service;

import com.ua.erent.module.core.di.Injector;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 11/7/2016.
 */

public abstract class InjectableService<S extends Service> extends Service {

    public <Component extends InjectableServiceComponent> InjectableService(@NotNull Class<Component> cl) {
        final Component component = Injector.injector().getComponent(this, cl);
        final S service = (S) this;
        component.inject(service);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Injector.injector().destroyComponent(this);
    }
}
