package com.ua.erent.module.core.util;

/**
 * Created by Максим on 10/13/2016.
 * <p>
 *     Represents abstract builder
 * </p>
 * @param <T> production type
 */
public interface IBuilder < T > {
    /**
     * creates a new instance of T
     * */
    T build();
}
