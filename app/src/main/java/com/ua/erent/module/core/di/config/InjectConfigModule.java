package com.ua.erent.module.core.di.config;

import com.ua.erent.module.core.account.auth.di.AuthComponent;
import com.ua.erent.module.core.app.di.AppComponent;
import com.ua.erent.module.core.di.Injector;
import com.ua.erent.module.core.presentation.mvp.component.CropComponent;
import com.ua.erent.module.core.presentation.mvp.component.DaggerCropComponent;
import com.ua.erent.module.core.presentation.mvp.component.DaggerInitialScreenComponent;
import com.ua.erent.module.core.presentation.mvp.component.DaggerLoginComponent;
import com.ua.erent.module.core.presentation.mvp.component.DaggerRegisterComponent;
import com.ua.erent.module.core.presentation.mvp.component.DaggerTestComponent;
import com.ua.erent.module.core.presentation.mvp.component.InitialScreenComponent;
import com.ua.erent.module.core.presentation.mvp.component.LoginComponent;
import com.ua.erent.module.core.presentation.mvp.component.RegisterComponent;
import com.ua.erent.module.core.presentation.mvp.component.TestComponent;
import com.ua.erent.module.core.presentation.mvp.module.CropModule;
import com.ua.erent.module.core.presentation.mvp.module.InitialScreenModule;
import com.ua.erent.module.core.presentation.mvp.module.LoginModule;
import com.ua.erent.module.core.presentation.mvp.module.RegisterModule;
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
    private final RegisterModule registerModule;
    private final InitialScreenModule initialScreenModule;

    public static class Builder implements IBuilder<InjectConfigModule> {

        private AppComponent appComponent;
        private AuthComponent authComponent;
        private LoginModule loginModule;
        private RegisterModule registerModule;
        private InitialScreenModule initialScreenModule;

        public Builder() {
        }

        public InitialScreenModule getInitialScreenModule() {
            return initialScreenModule;
        }

        public Builder setInitialScreenModule(InitialScreenModule initialScreenModule) {
            this.initialScreenModule = initialScreenModule;
            return this;
        }

        public RegisterModule getRegisterModule() {
            return registerModule;
        }

        public Builder setRegisterModule(RegisterModule registerModule) {
            this.registerModule = registerModule;
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
        this.registerModule = Preconditions.checkNotNull(builder.getRegisterModule());
        this.initialScreenModule = Preconditions.checkNotNull(builder.getInitialScreenModule());
    }

    @Override
    protected void configure(@NotNull Injector injector) {

        // signUp component factories to create component for injection
        injector
                .registerComponentFactory(TestComponent.class, () -> DaggerTestComponent.builder().appComponent(appComponent).
                        testModule(new TestModule()).build())
                .registerComponentFactory(LoginComponent.class, () -> DaggerLoginComponent.builder().
                        authComponent(authComponent).loginModule(loginModule).build())
                .registerComponentFactory(RegisterComponent.class, () -> DaggerRegisterComponent.builder().
                        authComponent(authComponent).registerModule(registerModule).build())
                .registerComponentFactory(InitialScreenComponent.class, () -> DaggerInitialScreenComponent.builder()
                        .authComponent(authComponent).initialScreenModule(initialScreenModule).build())
                .registerComponentFactory(CropComponent.class, () -> DaggerCropComponent.builder()
                        .cropModule(new CropModule()).build());
    }

}
