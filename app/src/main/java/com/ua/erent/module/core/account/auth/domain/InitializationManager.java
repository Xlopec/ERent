package com.ua.erent.module.core.account.auth.domain;

import com.ua.erent.module.core.util.Initializeable;

import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * Performs initial app initialization process
 * </p>
 * Created by Максим on 10/14/2016.
 */
public interface InitializationManager {

    interface ICallback {

        /**
         * Called before initialization execution
         */
        void onPreExecute();

        /**
         * Called when all components were initialized
         */
        void onInitialized();

        /**
         * Called each time new component was initialized successfully. Can be used
         * to monitor initialization progress
         *
         * @param initializeable newly initialized component
         * @param progress       components count which are already initialized
         * @param total          total components count
         */
        void onComponentInitialized(@NotNull Initializeable initializeable, int progress, int total);

        /**
         * Exception occurred while initializing component
         *
         * @param initializeable component which caused exception
         * @param th             exception cause
         */
        void onException(@NotNull Initializeable initializeable, @NotNull Throwable th);

        /**
         * Critical exception occurred, abort
         * initialization process
         *
         * @param initializeable component which caused failure
         * @param th             failure cause
         */
        void onFailure(@NotNull Initializeable initializeable, @NotNull Throwable th);

    }

    /**
     * Runs initialization process asynchronously
     *
     * @param callback callback to process
     *                 initialization progress
     */
    void initialize(@NotNull ICallback callback);

}
