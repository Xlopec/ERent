package com.ua.erent.module.core.app.domain.interfaces;

import com.ua.erent.module.core.app.domain.ComponentKind;

import org.jetbrains.annotations.NotNull;

import rx.Observable;

/**
 * <p>
 * Handles application lifecycle changes, e.g. you can register
 * observer to monitor app visibility or add callback to be notified
 * when component should save its state
 * </p>
 * Created by Максим on 10/28/2016.
 */

public interface IAppLifecycleManager {
    /**
     * Callback to monitor save/restore component events
     */
    interface IStateCallback {
        /**
         * <p>
         * Gets invoked when app becomes visible
         * </p>
         * <p>
         * By implementing this callback component can
         * restore its state; <b>Note, that in order to keep app responsive
         * do not start heavy operation here</b>
         * </p>
         */
        void onRestoreState();

        /**
         * <p>
         * Gets invoked when app becomes invisible and may be killed by the system.
         * </p>
         * <p>
         * By implementing this callback component can
         * save its state; <b>Note, that in order to keep app responsive
         * do not start heavy operation here</b>
         * </p>
         */
        void onSaveState();

    }

    /**
     * Registers callback to be notified about app state changes
     *
     * @param kind     kind of component. Each kind differs in its priority, e.g.
     *                 component tagged with {@linkplain ComponentKind#APP_SERVICE} will be restored
     *                 earlier than {@linkplain ComponentKind#VIEW_MODEL}
     * @param callback callback to register
     */
    void registerCallback(@NotNull ComponentKind kind, @NotNull IStateCallback callback);

    /**
     * Unregisters callback previously registered in {@link #registerCallback(ComponentKind, IStateCallback)}
     *
     * @param callback callback to unregister
     */
    void unregisterCallback(@NotNull IStateCallback callback);

    /**
     * @return rx observable to monitor app visible/invisible transition
     */
    Observable<Boolean> getAppForegroundObservable();

    /**
     * @return true if app is currently is foreground and false in another case
     */
    boolean isAppForeground();

}
