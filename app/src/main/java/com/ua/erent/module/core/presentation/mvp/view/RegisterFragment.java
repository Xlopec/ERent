package com.ua.erent.module.core.presentation.mvp.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.ua.erent.R;
import com.ua.erent.module.core.di.target.InjectableV4Fragment;
import com.ua.erent.module.core.presentation.mvp.component.RegisterComponent;
import com.ua.erent.module.core.presentation.mvp.model.SignUpForm;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IRegisterPresenter;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.IRegisterView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Максим on 10/28/2016.
 */

public final class RegisterFragment extends InjectableV4Fragment<RegisterFragment, IRegisterPresenter>
        implements IRegisterView, Validator.ValidationListener {

    private static final String TAG = RegisterFragment.class.getSimpleName();

    private static final int REQ_CROP_IMAGE = 123;

    @BindView(R.id.email_fld)
    @Email
    protected EditText emailEditText;

    @BindView(R.id.username_fld)
    @Pattern(regex = "^\\w{5,20}$")
    protected EditText usernameEditText;

    @BindView(R.id.password_fld)
    @Password(scheme = Password.Scheme.ALPHA)
    @Length(min = 6, max = 20)
    protected EditText passwordEditText;

    @BindView(R.id.conf_password_fld)
    @ConfirmPassword(message = "Passwords don't match")
    protected EditText confPasswordEditText;

    @BindView(R.id.register_btn)
    protected Button registerBtn;

    private final Validator validator;

    private ProgressDialog progressDialog;

    public RegisterFragment() {
        super(RegisterComponent.class);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        progressDialog = new ProgressDialog(getActivity(), R.style.DefaultDialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int id = item.getItemId();

        if (id == android.R.id.home) {
            presenter.onNavigateLogin();
        }

        return true;
    }

    @NotNull
    @Override
    public Context getApplicationContext() {
        return getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerBtn.setOnClickListener(v -> validator.validate());
    }

    @Override
    public void onValidationSucceeded() {
        presenter.onCreateAccount(new SignUpForm()
                .setUsername(usernameEditText.getText().toString())
                .setEmail(emailEditText.getText().toString())
                .setPassword(passwordEditText.getText().toString())
                .setConfPassword(confPasswordEditText.getText().toString())
        );
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

    @Override
    public void showProgress(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
