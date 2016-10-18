package com.ua.erent.module.core.di.util;

import javax.inject.Provider;

/**
 * <p>
 * Defines factory which creates
 * subclasses of {@linkplain Provider}.
 * Such factory can be used to configure dependency injection
 * </p>
 * <p><b>Component</b> is plain dagger component</p>
 * Created by Максим on 10/11/2016.
 */
public interface ComponentFactory< Component > {
    /**
     * @return new instance of {@linkplain Component}
     */
    Component create();

}
