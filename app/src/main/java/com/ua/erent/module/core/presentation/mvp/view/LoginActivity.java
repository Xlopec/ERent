package com.ua.erent.module.core.presentation.mvp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.ua.erent.R;
import com.ua.erent.module.core.presentation.mvp.component.LoginComponent;
import com.ua.erent.module.core.presentation.mvp.core.IBaseView;
import com.ua.erent.module.core.presentation.mvp.core.InjectableActivity;
import com.ua.erent.module.core.presentation.mvp.presenter.ILoginPresenter;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

/**
 * A login screen that offers login via email/password.
 */
public final class LoginActivity extends InjectableActivity<LoginActivity, ILoginPresenter> implements IBaseView {

    // UI references.
    @BindView(R.id.email) protected AutoCompleteTextView mEmailView;
    @BindView(R.id.password) protected EditText mPasswordView;
    @BindView(R.id.email_sign_in_button) protected Button loginBtn;

    public LoginActivity() {
       super(R.layout.activity_login, LoginComponent.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBtn.setOnClickListener(v -> presenter.onLogin(mEmailView.getText().toString(),
                mPasswordView.getText().toString()));
    }

    @NotNull
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}

