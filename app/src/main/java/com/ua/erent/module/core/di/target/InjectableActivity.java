package com.ua.erent.module.core.di.target;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.ua.erent.module.core.presentation.mvp.core.IBasePresenter;
import com.ua.erent.module.core.presentation.mvp.core.IBaseView;
import com.ua.erent.module.core.presentation.mvp.core.IMVPComponent;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.internal.Preconditions;

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

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    /**
     * Subclass of {@linkplain IBaseView} which represents view
     */
    private final View view;
    /**
     * Presenter which handles this view
     */
    @Inject
    protected Presenter presenter;
    /**
     * View layout resource id
     */
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
        Preconditions.checkNotNull(presenter,
                String.format("Presenter wasn't injected, check whether you've specified correct inject" +
                                " target type for view %s (no subclasses allowed) in component %s",
                        getClass().getName(), cl.getName()));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        presenter.attachView(view, getIntent().getExtras(), savedInstanceState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setup layout view
        setContentView(layoutResId);
        // bind view fields
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onDestroy() {

        try {
            presenter.onDestroy();
        } finally {
            super.onDestroy();
            injector().destroyComponent(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        try {
            presenter.onSaveInstanceState(outState);
        } finally {
            super.onSaveInstanceState(outState);
        }
    }

}
