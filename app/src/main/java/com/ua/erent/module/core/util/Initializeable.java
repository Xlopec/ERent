package com.ua.erent.module.core.util;

import android.os.Bundle;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 10/14/2016.
 */

public interface Initializeable {

    interface ICallback {

        void onInitialized();

        void onFailure(@NotNull Throwable th);

    }

    void initialize(@NotNull Bundle arguments, @NotNull ICallback callback);

}
