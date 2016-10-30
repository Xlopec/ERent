package com.ua.erent.module.core.app.domain;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.ua.erent.module.core.app.domain.interfaces.IAppLifecycleManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Максим on 10/28/2016.
 */

public final class AppLifecycleManager implements IAppLifecycleManager {

    private final Map<ComponentKind, Collection<IStateCallback>> kindToCallbacks;
    private final AppLifecycle appLifecycle;
    private boolean isInit;

    private static final class AppLifecycle implements Application.ActivityLifecycleCallbacks {

        private final Set<String> activities;
        private final PublishSubject<Boolean> foregroundObs;
        private boolean isForeground;

        AppLifecycle() {
            this.activities = new HashSet<>();
            foregroundObs = PublishSubject.create();
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            activities.add(activity.getLocalClassName());

            if (savedInstanceState != null) {
                updateObservable();
            } else {
                isForeground = true;
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            activities.add(activity.getLocalClassName());
            updateObservable();
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
            minusActivity(activity);
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            minusActivity(activity);
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }

        private void updateObservable() {

            if (isAppForeground() != isForeground) {
                isForeground = isAppForeground();
                foregroundObs.onNext(isForeground);
            }
        }

        private void minusActivity(Activity activity) {
            activities.remove(activity.getLocalClassName());
            updateObservable();
        }

        boolean isAppForeground() {
            return !activities.isEmpty();
        }

        Observable<Boolean> getForegroundObs() {
            return foregroundObs.asObservable();
        }

    }

    @Inject
    public AppLifecycleManager(Application application) {
        Preconditions.checkNotNull(application);
        appLifecycle = new AppLifecycle();
        kindToCallbacks = new EnumMap<>(ComponentKind.class);
        appLifecycle.getForegroundObs().filter(foreground -> !isInit || !foreground)
                .subscribe(foreground -> {
                    isInit = true;
                    fireCallbacks(foreground);
                });
        application.registerActivityLifecycleCallbacks(appLifecycle);
    }

    @Override
    public void registerCallback(@NotNull ComponentKind kind, @NotNull IStateCallback callback) {

        Preconditions.checkNotNull(kind);
        Preconditions.checkNotNull(callback);

        synchronized (kindToCallbacks) {

            Collection<IStateCallback> callbacks = kindToCallbacks.get(kind);

            if (callbacks == null) {
                callbacks = new ArrayList<>(1);
            }

            if (callbacks.add(callback)) {
                kindToCallbacks.put(kind, callbacks);
            }
        }
    }

    @Override
    public void unregisterCallback(@NotNull IStateCallback callback) {

        synchronized (kindToCallbacks) {

            for (final ComponentKind kind : kindToCallbacks.keySet()) {
                if (kindToCallbacks.get(kind).remove(callback)) return;
            }
        }
    }

    @Override
    public Observable<Boolean> getAppForegroundObservable() {
        return appLifecycle.getForegroundObs();
    }

    @Override
    public boolean isAppForeground() {
        return !appLifecycle.isAppForeground();
    }

    private void fireCallbacks(boolean restoreState) {

        synchronized (kindToCallbacks) {

            for (final ComponentKind kind : ComponentKind.prioritySet()) {

                final Collection<IStateCallback> callbacks = kindToCallbacks.get(kind);

                if (callbacks != null) {
                    for (final IStateCallback callback : callbacks) {

                        if (restoreState) {
                            callback.onRestoreState();
                        } else {
                            callback.onSaveState();
                        }
                    }
                }
            }
        }
    }

}
