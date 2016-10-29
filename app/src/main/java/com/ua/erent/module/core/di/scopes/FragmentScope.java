package com.ua.erent.module.core.di.scopes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * <p>
 *     One instance per fragment
 * </p>
 * Created by Максим on 10/28/2016.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface FragmentScope {}
