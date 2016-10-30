package com.ua.erent.module.core.presentation.mvp.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ua.erent.module.core.presentation.mvp.core.InjectableV4Fragment;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ILoginPresenter;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.ILoginView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import rx.Observable;

/**
 * A signIn screen that offers signIn via email/password.
 */
public final class LoginFragment extends InjectableV4Fragment<LoginFragment, ILoginPresenter>
        implements ILoginView, Validator.ValidationListener {

    @Pattern(regex = "^([_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,}))|(\\w{5,20})$")
    @BindView(R.id.login_fld)
    protected EditText loginEditText;

    @Password(scheme = Password.Scheme.ALPHA)
    @Length(min = 6, max = 20)
    @BindView(R.id.password_fld)
    protected EditText passwordEditText;

    @BindView(R.id.login_btn)
    protected Button loginBtn;

    @BindView(R.id.register_link)
    protected TextView registerTextView;

    @BindView(R.id.login_pre_loader_root)
    protected View preLoaderRootView;
    @BindView(R.id.pre_load_status)
    protected TextView preLoaderInfoTextView;

    private final Validator validator;
    private ProgressDialog progressBar;

    public LoginFragment() {
        super(LoginComponent.class);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressBar = new ProgressDialog(getActivity());
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginBtn.setOnClickListener(v -> validator.validate());
        registerTextView.setOnClickListener(v -> presenter.onNavigateCreateAccount());
    }

    @NotNull
    @Override
    public Context getContext() {
        return getActivity();
    }

    @NotNull
    @Override
    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
            final String message = error.getCollatedErrorMessage(getActivity());

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        }
    }

}

