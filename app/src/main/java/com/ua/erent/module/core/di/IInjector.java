package com.ua.erent.module.core.di;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 10/11/2016.
 */
public interface IInjector <T> {

    T getComponent(@NotNull Class<?> key);

}
