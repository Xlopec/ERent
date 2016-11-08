package com.ua.erent.module.core.di.scopes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * <p>
 * One instance per service
 * </p>
 * Created by Максим on 11/7/2016.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceScope {}
