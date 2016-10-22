package com.ua.erent.module.core.di.config;

import com.ua.erent.module.core.account.auth.component.AuthComponent;
import com.ua.erent.module.core.app.component.AppComponent;
import com.ua.erent.module.core.di.Injector;
import com.ua.erent.module.core.presentation.mvp.component.DaggerLoginComponent;
import com.ua.erent.module.core.presentation.mvp.component.DaggerTestComponent;
import com.ua.erent.module.core.presentation.mvp.component.LoginComponent;
import com.ua.erent.module.core.presentation.mvp.component.TestComponent;
import com.ua.erent.module.core.presentation.mvp.module.LoginModule;
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

    public static class Builder implements IBuilder<InjectConfigModule> {

        private AppComponent appComponent;
        private AuthComponent authComponent;
        private LoginModule loginModule;

        public Builder() {
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
    }

    @Override
    protected void configure(@NotNull Injector injector) {

        // register component factories to create component for injection
        injector
                .registerComponentFactory(TestComponent.class, () -> DaggerTestComponent.builder().appComponent(appComponent).
                        testModule(new TestModule()).build())
                .registerComponentFactory(LoginComponent.class, () -> DaggerLoginComponent.builder().
                        authComponent(authComponent).loginModule(loginModule).build());
    }

}
