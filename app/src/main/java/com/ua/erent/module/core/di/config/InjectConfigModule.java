package com.ua.erent.module.core.di.config;

import com.ua.erent.module.core.app.AppComponent;
import com.ua.erent.module.core.di.Injector;
import com.ua.erent.module.core.item.di.SyncServiceComponent;
import com.ua.erent.module.core.presentation.mvp.component.CategoriesComponent;
import com.ua.erent.module.core.presentation.mvp.component.CropComponent;
import com.ua.erent.module.core.presentation.mvp.component.DaggerCategoriesComponent;
import com.ua.erent.module.core.presentation.mvp.component.DaggerCropComponent;
import com.ua.erent.module.core.presentation.mvp.component.DaggerInitialScreenComponent;
import com.ua.erent.module.core.presentation.mvp.component.DaggerLoginComponent;
import com.ua.erent.module.core.presentation.mvp.component.DaggerItemsComponent;
import com.ua.erent.module.core.presentation.mvp.component.DaggerRegisterComponent;
import com.ua.erent.module.core.presentation.mvp.component.InitialScreenComponent;
import com.ua.erent.module.core.presentation.mvp.component.ItemsComponent;
import com.ua.erent.module.core.presentation.mvp.component.LoginComponent;
import com.ua.erent.module.core.presentation.mvp.component.RegisterComponent;
import com.ua.erent.module.core.presentation.mvp.module.CategoriesModule;
import com.ua.erent.module.core.presentation.mvp.module.CropModule;
import com.ua.erent.module.core.presentation.mvp.module.InitialScreenModule;
import com.ua.erent.module.core.presentation.mvp.module.LoginModule;
import com.ua.erent.module.core.presentation.mvp.module.RegisterModule;
import com.ua.erent.module.core.util.IBuilder;

import org.jetbrains.annotations.NotNull;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 10/11/2016.
 */
public final class InjectConfigModule extends Injector.IConfigModule {

    private final AppComponent appComponent;
    private final SyncServiceComponent syncServiceComponent;

    public static class Builder implements IBuilder<InjectConfigModule> {

        private AppComponent appComponent;
        private SyncServiceComponent syncServiceComponent;

        public Builder() {
        }

        public SyncServiceComponent getSyncServiceComponent() {
            return syncServiceComponent;
        }

        public Builder setSyncServiceComponent(SyncServiceComponent syncServiceComponent) {
            this.syncServiceComponent = syncServiceComponent;
            return this;
        }

        public AppComponent getAppComponent() {
            return appComponent;
        }

        public Builder setAppComponent(AppComponent appComponent) {
            this.appComponent = appComponent;
            return this;
        }

        @Override
        public InjectConfigModule build() {
            return new InjectConfigModule(this);
        }
    }

    private InjectConfigModule(Builder builder) {
        this.appComponent = Preconditions.checkNotNull(builder.getAppComponent());
        this.syncServiceComponent = Preconditions.checkNotNull(builder.getSyncServiceComponent());
    }

    @Override
    protected void configure(@NotNull Injector injector) {

        // register component factories to create component target
        // for further injection
        injector
                .registerComponentFactory(ItemsComponent.class, () -> DaggerItemsComponent.builder().
                        categoriesModule(new CategoriesModule()).appComponent(appComponent).build())
                .registerComponentFactory(LoginComponent.class, () -> DaggerLoginComponent.builder()
                        .appComponent(appComponent).loginModule(new LoginModule()).build())
                .registerComponentFactory(RegisterComponent.class, () -> DaggerRegisterComponent.builder()
                        .appComponent(appComponent).registerModule(new RegisterModule()).build())
                .registerComponentFactory(InitialScreenComponent.class, () -> DaggerInitialScreenComponent.builder()
                        .appComponent(appComponent).initialScreenModule(new InitialScreenModule()).build())
                .registerComponentFactory(CropComponent.class, () -> DaggerCropComponent.builder()
                        .cropModule(new CropModule()).build())
                .registerComponentFactory(CategoriesComponent.class, () -> DaggerCategoriesComponent.builder()
                        .appComponent(appComponent).categoriesModule(new CategoriesModule()).build())
                .registerComponentFactory(SyncServiceComponent.class, () -> syncServiceComponent);
    }

}
