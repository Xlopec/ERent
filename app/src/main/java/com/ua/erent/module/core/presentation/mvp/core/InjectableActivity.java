package com.ua.erent.module.core.presentation.mvp.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.ua.erent.module.core.di.Injector.injector;

/**
 * <p>
 * Base class for all injectable activities. This class handles basic view-presenter
 * interaction and provides injection in the constructor for all non-view components which
 * were registered in specified Dagger component.
 * </p>
 * <p>
 * All view components are bound in {@link #onCreate(Bundle)} and available after this method returns
 * </p>
 * Created by Максим on 10/14/2016.
 */
public class InjectableActivity<View extends IBaseView, Presenter extends IBasePresenter<View>>
        extends AppCompatActivity {

    private static final String TAG = InjectableActivity.class.getSimpleName();
    /**
     * Subclass of {@linkplain IBaseView} which represents view
     */
    private final View view;
    /**
     * Presenter which handles this view
     */
    @Inject protected Presenter presenter;
    /**
     * View layout resource id
     * */
    private final int layoutResId;

    /**
     * Constructs a new instance of injectable activity. To specify which component
     * should be injected by injector just pass corresponding component class
     *
     * @param cl component class to inject. Such component should be subtype of {@linkplain IMVPComponent}
     */
    public <Component extends IMVPComponent<View, Presenter>> InjectableActivity(final int layoutResId, @NotNull Class<Component> cl) {

        final Component component = injector().getComponent(this, cl);
        // fields injection
        view = (View) this;
        this.layoutResId = layoutResId;
        component.inject(view);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            // setup layout view
            setContentView(layoutResId);
            // bind view fields
            ButterKnife.bind(this);
            presenter.attachView(view, getIntent().getExtras(), savedInstanceState);
        } catch (final Exception exc) {
            Log.e(TAG, "exception in #onCreate", exc);
        }
    }

    @Override
    protected void onResume() {

        try {
            super.onResume();
            presenter.onResume();
        } catch (final Exception exc) {
            Log.e(TAG, "exception in #onResume", exc);
        }
    }

    @Override
    protected void onPause() {

        try {
            super.onPause();
            presenter.onPause();
        } catch (final Exception exc) {
            Log.e(TAG, "exception in #onPause", exc);
        }
    }

    @Override
    protected void onDestroy() {

        try {
            presenter.onDestroy();
        } catch (final Exception exc) {
            Log.e(TAG, "exception in #onDestroy", exc);
        } finally {
            injector().destroyComponent(this);
            super.onDestroy();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        try {
            presenter.onSaveInstanceState(outState);
        } catch (final Exception exc) {
            Log.e(TAG, "exception in #onSaveInstanceState", exc);
        } finally {
            super.onSaveInstanceState(outState);
        }
    }

}
