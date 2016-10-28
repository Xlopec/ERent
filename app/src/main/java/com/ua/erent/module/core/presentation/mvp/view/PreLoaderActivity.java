package com.ua.erent.module.core.presentation.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ua.erent.R;
import com.ua.erent.module.core.presentation.mvp.component.PreLoaderComponent;
import com.ua.erent.module.core.presentation.mvp.core.IBaseView;
import com.ua.erent.module.core.presentation.mvp.core.InjectableActivity;
import com.ua.erent.module.core.presentation.mvp.presenter.IPreLoadPresenter;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

/**
 * Created by Максим on 10/27/2016.
 */

public final class PreLoaderActivity extends InjectableActivity<PreLoaderActivity, IPreLoadPresenter> implements IBaseView {

    @BindView(R.id.pre_progress_bar) protected ProgressBar progressBar;
    @BindView(R.id.pre_load_status) protected TextView progressStatusTextView;

    public PreLoaderActivity() {
        super(R.layout.activity_pre_loader, PreLoaderComponent.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(() ->
                PreLoaderActivity.this.startActivity(new Intent(PreLoaderActivity.this, LoginActivity.class)), 4000L);
    }

    @NotNull
    @Override
    public Context getContext() {
        return this;
    }
}
