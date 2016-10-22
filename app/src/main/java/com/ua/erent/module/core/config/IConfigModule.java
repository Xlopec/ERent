package com.ua.erent.module.core.config;

/**
 * <p>
 *     Class which represents independent config module
 * </p>
 * Created by Максим on 10/12/2016.
 */
public abstract class IConfigModule < T > {
    /**
     * Subclasses should implement this method to provide their own
     * configuration
     * */
    public abstract T configure();

}