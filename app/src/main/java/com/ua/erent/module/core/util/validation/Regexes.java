package com.ua.erent.module.core.util.validation;

/**
 * Created by Максим on 11/4/2016.
 */

public final class Regexes {

    public static final String SIGN_IN_USERNAME = "^([_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,}))|(\\w{5,20})$";
    public static final String USERNAME = "^\\w{5,20}$";
    public static final String PASSWORD = "^\\w{5,20}$";

}