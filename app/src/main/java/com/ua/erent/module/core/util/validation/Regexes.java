package com.ua.erent.module.core.util.validation;

/**
 * Created by Максим on 11/4/2016.
 */

public final class Regexes {

    public static final String SIGN_IN_USERNAME = "^\\w{5,20}$";
            //"^([_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,}))|(\\w{5,20})$";
    public static final String USERNAME = "^\\w{4,20}$";
    public static final String PASSWORD = "^\\w{5,20}$";
    public static final String ITEM_NAME = "^\\(\\w\\s?){3,256}$";
    public static final String ITEM_DESCRIPTION = "^(\\w[\\s\\,\\.\'\"\\-]?){3,4000}$";
    public static final String CATEGORY_NAME = "^(\\w[\\s\\-]?){3,50}$";
    public static final String CATEGORY_DESCRIPTION= "(\\w[\\s\\-]?){3,50}";

    private Regexes() {
        throw new RuntimeException();
    }

}
