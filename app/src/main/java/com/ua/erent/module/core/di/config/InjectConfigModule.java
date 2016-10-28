package com.ua.erent.module.core.di.config;

import com.ua.erent.module.core.account.auth.di.AuthComponent;
import com.ua.erent.module.core.app.di.AppComponent;
import com.ua.erent.module.core.di.Injector;
import com.ua.erent.module.core.presentation.mvp.component.DaggerLoginComponent;
import com.ua.erent.module.core.presentation.mvp.component.DaggerPreLoaderComponent;
import com.ua.erent.module.core.presentation.mvp.component.DaggerTestComponent;
import com.ua.erent.module.core.presentation.mvp.component.LoginComponent;
import com.ua.erent.module.core.presentation.mvp.component.PreLoaderComponent;
import com.ua.erent.module.core.presentation.mvp.component.TestComponent;
import com.ua.erent.module.core.presentation.mvp.module.LoginModule;
import com.ua.erent.module.core.presentation.mvp.module.PreLoaderModule;
import com.ua.erent.module.core.presentation.mvp.module.TestModule;
import com.ua.erent.module.core.util.IBuilder;

import org.jetbrains.annotations.NotNull;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 10/11/2016.
 */
public final class InjectConfigModule extends Injector.IConfigModule {

    private final AppComponent appComponent;
    private final AuthComponent authComponent;
    private final LoginModule loginModule;
    private final PreLoaderModule preLoaderModule;

    public static class Builder implements IBuilder<InjectConfigModule> {

        private AppComponent appComponent;
        private AuthComponent authComponent;
        private LoginModule loginModule;
        private PreLoaderModule preLoaderModule;

        public Builder() {
        }

        public PreLoaderModule getPreLoaderModule() {
            return preLoaderModule;
        }

        public Builder setPreLoaderModule(PreLoaderModule preLoaderModule) {
            this.preLoaderModule = preLoaderModule;
            return this;
        }

        public LoginModule getLoginModule() {
            return loginModule;
        }

        public Builder setLoginModule(LoginModule loginModule) {
            this.loginModule = loginModule;
            return this;
        }

        public AppComponent getAppComponent() {
            return appComponent;
        }

        public Builder setAppComponent(AppComponent appComponent) {
            this.appComponent = appComponent;
            return this;
        }

        public AuthComponent getAuthComponent() {
            return authComponent;
        }

        public Builder setAuthComponent(AuthComponent authComponent) {
            this.authComponent = authComponent;
            return this;
        }

        @Override
        public InjectConfigModule build() {
            return new InjectConfigModule(this);
        }
    }

    private InjectConfigModule(Builder builder) {
        this.appComponent = Preconditions.checkNotNull(builder.getAppComponent());
        this.authComponent = Preconditions.checkNotNull(builder.getAuthComponent());
        this.loginModule = Preconditions.checkNotNull(builder.getLoginModule());
        this.preLoaderModule = Preconditions.checkNotNull(builder.getPreLoaderModule());
    }

    @Override
    protected void configure(@NotNull Injector injector) {

        // register component factories to create component for injection
        injector
                .registerComponentFactory(TestComponent.class, () -> DaggerTestComponent.builder().appComponent(appComponent).
                        testModule(new TestModule()).build())
                .registerComponentFactory(LoginComponent.class, () -> DaggerLoginComponent.builder().
                        authComponent(authComponent).loginModule(loginModule).build())
                .registerComponentFactory(PreLoaderComponent.class, () -> DaggerPreLoaderComponent.builder().
                        authComponent(authComponent).preLoaderModule(preLoaderModule).build());
    }

}
