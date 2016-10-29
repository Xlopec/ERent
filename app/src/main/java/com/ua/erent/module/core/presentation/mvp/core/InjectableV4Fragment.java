package com.ua.erent.module.core.presentation.mvp.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.internal.Preconditions;

import static com.ua.erent.module.core.di.Injector.injector;

/**
 * <p>
 * Base class for injectable fragments which derives from {@linkplain Fragment}.
 * This class handles basic view-presenter
 * interaction and provides injection in the constructor for all non-view components which
 * were registered in specified Dagger component.
 * </p>
 * <p>
 * All view components are bound in {@link #onViewCreated(android.view.View, Bundle)} and available after this method returns
 * </p>
 * Created by Максим on 10/28/2016.
 */

public abstract class InjectableV4Fragment<View extends IBaseView, Presenter extends IBasePresenter<View>>
        extends Fragment {

    private static final String TAG = InjectableActivity.class.getSimpleName();
    /**
     * Presenter which handles this view
     */
    @Inject
    protected Presenter presenter;
    private final View view;
    private Class<? extends IMVPComponent<View, Presenter>> cl;

    /**
     * Constructs a new instance of injectable activity. To specify which component
     * should be injected by injector just pass corresponding component class
     *
     * @param cl component class to inject. Such component should be subtype of {@linkplain IMVPComponent}
     */
    public <Component extends IMVPComponent<View, Presenter>> InjectableV4Fragment(@NotNull Class<Component> cl) {
        this.cl = Preconditions.checkNotNull(cl);
        // fields injection
        view = (View) this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);

            final IMVPComponent<View, Presenter> component = injector().getComponent(getActivity(), cl);

            component.inject(view);
        } catch (final Exception exc) {
            Log.e(TAG, "exception in #onCreate", exc);
        }
    }

    @Override
    public void onViewCreated(android.view.View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // view component hierarchy has been fully instantiated
        // and can be managed by presenter
        presenter.attachView(view, getArguments(), savedInstanceState);
    }

    @Override
    public void onResume() {

        try {
            super.onResume();
            presenter.onResume();
        } catch (final Exception exc) {
            Log.e(TAG, "exception in #onResume", exc);
        }
    }

    @Override
    public void onPause() {

        try {
            super.onPause();
            presenter.onPause();
        } catch (final Exception exc) {
            Log.e(TAG, "exception in #onPause", exc);
        }
    }

    @Override
    public void onDestroy() {

        try {
            presenter.onDestroy();
        } catch (final Exception exc) {
            Log.e(TAG, "exception in #onSaveState", exc);
        } finally {
            injector().destroyComponent(getContext());
            super.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        try {
            presenter.onSaveInstanceState(outState);
        } catch (final Exception exc) {
            Log.e(TAG, "exception in #onSaveInstanceState", exc);
        } finally {
            super.onSaveInstanceState(outState);
        }
    }

}
