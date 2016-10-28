package com.ua.erent.module.core.presentation.mvp.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.ua.erent.R;
import com.ua.erent.module.core.presentation.mvp.component.LoginComponent;
import com.ua.erent.module.core.presentation.mvp.core.InjectableActivity;
import com.ua.erent.module.core.presentation.mvp.presenter.ILoginPresenter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import rx.Observable;

/**
 * A login screen that offers login via email/password.
 */
public final class LoginActivity extends InjectableActivity<LoginActivity, ILoginPresenter>
        implements ILoginView, Validator.ValidationListener {

    // UI references.
    @Pattern(regex = "^([_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,}))|(\\w{5,20})$")
    @BindView(R.id.login_fld)
    protected EditText loginEditText;

    @Password(scheme = Password.Scheme.ALPHA_NUMERIC)
    @Length(sequence = 2, min = 6, max = 20)
    @BindView(R.id.password_fld)
    protected EditText passwordEditText;

    @BindView(R.id.login_btn)
    protected Button loginBtn;

    @BindView(R.id.login_pre_loader_root)
    protected View preLoaderRootView;
    @BindView(R.id.pre_load_status)
    protected TextView preLoaderInfoTextView;

    private final Validator validator;
    private ProgressDialog progressBar;

    public LoginActivity() {
        super(R.layout.activity_login, LoginComponent.class);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        progressBar = new ProgressDialog(this);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        loginBtn.setOnClickListener(v -> validator.validate());
    }

    @NotNull
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int id = item.getItemId();

        if (id == R.id.action_web) {
            // open web browser
        } else if (id == R.id.action_settings) {
            // open settings
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgressBar(String message) {
        progressBar.setMessage(message);
        progressBar.show();
    }

    @Override
    public void hideProgressBar() {
        progressBar.dismiss();
    }

    @Override
    public void showProgressView() {
        preLoaderRootView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setProgressInfo(String info) {
        preLoaderInfoTextView.setText(info);
    }

    @Override
    public void hideProgressView() {
        preLoaderRootView.setVisibility(View.GONE);
    }

    @Override
    public void onValidationSucceeded() {

        final Observable<String> progressObs = presenter.onLogin(loginEditText.getText().toString(),
                passwordEditText.getText().toString());

        progressObs.subscribe(this::setProgressInfo);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (final ValidationError error : errors) {

            final View view = error.getView();
            final String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

}

