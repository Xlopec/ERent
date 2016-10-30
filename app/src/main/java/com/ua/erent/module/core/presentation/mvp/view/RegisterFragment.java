package com.ua.erent.module.core.presentation.mvp.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.ua.erent.R;
import com.ua.erent.module.core.presentation.mvp.component.RegisterComponent;
import com.ua.erent.module.core.presentation.mvp.core.InjectableV4Fragment;
import com.ua.erent.module.core.presentation.mvp.model.SignUpForm;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IRegisterPresenter;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.IRegisterView;
import com.ua.erent.module.core.presentation.mvp.view.util.MyCircleImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.theartofdev.edmodo.cropper.CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE;
import static com.theartofdev.edmodo.cropper.CropImage.getCameraIntents;
import static com.theartofdev.edmodo.cropper.CropImage.getGalleryIntents;

/**
 * Created by Максим on 10/28/2016.
 */

public final class RegisterFragment extends InjectableV4Fragment<RegisterFragment, IRegisterPresenter>
        implements IRegisterView, Validator.ValidationListener {

    private static final String TAG = RegisterFragment.class.getSimpleName();

    private static final int REQ_CROP_IMAGE = 123;

    @BindView(R.id.user_avatar_select)
    protected MyCircleImageView userAvatarImageView;

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

    private boolean isDrawn;

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
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);


        final View layout = inflater.inflate(R.layout.fragment_register, container, false);
        final ViewTreeObserver vto = layout.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(() -> isDrawn = true);
        return layout;
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
        userAvatarImageView.setImageResource(R.drawable.ic_add_user_avatar);
        userAvatarImageView.setBorderWidth(0);
        registerBtn.setOnClickListener(v -> validator.validate());
        userAvatarImageView.setOnClickListener(v -> startActivityForResult(
                getPickImageChooserIntent(getContext(), "Pick avatar image", false), PICK_IMAGE_CHOOSER_REQUEST_CODE)
        );
    }

    public static Intent getPickImageChooserIntent(@NonNull Context context, CharSequence title, boolean includeDocuments) {

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();

        // collect all camera intents
        allIntents.addAll(getCameraIntents(context, packageManager));

        List<Intent> galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_GET_CONTENT, includeDocuments);
        if (galleryIntents.size() == 0) {
            // if no intents found for get-content try pick intent action (Huawei P9).
            galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_PICK, includeDocuments);
        }
        allIntents.addAll(galleryIntents);

        final Intent target = allIntents.get(allIntents.size() - 1);
        allIntents.remove(allIntents.size() - 1);

        // Create a chooser from the main  intent
        final Intent chooserIntent = Intent.createChooser(target, title);

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDrawn = false;
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
    public void setAvatarUri(@NotNull Uri uri) {
        handleAvatarUri(uri);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == PICK_IMAGE_CHOOSER_REQUEST_CODE) {

                final Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);
                final Intent intent = new Intent(getActivity(), ImageCropActivity.class);

                intent.putExtra(ImageCropActivity.ARG_IMG_URI, imageUri);
                intent.putExtra(ImageCropActivity.ARG_FIXED_ASPECT_RATIO, true);
                intent.putExtra(ImageCropActivity.ARG_ASPECT_RATIO_X, 1);
                intent.putExtra(ImageCropActivity.ARG_ASPECT_RATIO_Y, 1);
                intent.putExtra(ImageCropActivity.ARG_SHAPE, ImageCropActivity.Shape.OVAL);
                startActivityForResult(intent, REQ_CROP_IMAGE);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                final CropImage.ActivityResult result = CropImage.getActivityResult(data);
                handleAvatarUri(result.getUri());

            } else if(requestCode == REQ_CROP_IMAGE) {
                handleAvatarUri(data.getParcelableExtra(ImageCropActivity.ARG_CROPPED_IMG_URI));
            } else {
                Log.w(TAG, "Unknown request code " + requestCode);
            }

        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
            final Exception error = result.getError();
            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
        } else {
            Log.w(TAG, "Unknown result code " + resultCode);
        }
    }

    private void handleAvatarUri(@NotNull Uri uri) {

        try {

            final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

            if(isDrawn) {
                handleAvatarUri(uri, bitmap, userAvatarImageView.getWidth(), userAvatarImageView.getHeight());
            } else {
                userAvatarImageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        userAvatarImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                        handleAvatarUri(uri, bitmap, userAvatarImageView.getWidth(), userAvatarImageView.getHeight());
                        return true;
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void handleAvatarUri(Uri uri, Bitmap bitmap, int h, int w) {

        presenter.resizeAvatarBitmap(uri, bitmap, h, w).subscribe(
                bm -> {
                    userAvatarImageView.setBorderWidth(2);
                    userAvatarImageView.setImageBitmap(bm);
                },
                th -> showToast(th.getMessage())
        );
    }

}
