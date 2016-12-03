package com.ua.erent.module.core.util;

/**
 * Created by Максим on 12/1/2016.
 */

public final class MyTextUtil {

    private MyTextUtil() {
        throw new RuntimeException();
    }

    public static String capitalize(final String text) {

        if(text == null)
            throw new NullPointerException("text == null");

        if(text.length() == 0) return text;

        final char upperCase = Character.toUpperCase(text.charAt(0));
        return text.length() >= 2 ? upperCase + text.substring(1) : String.valueOf(upperCase);
    }

    public static String valueOrEmpty(final String str) {
        return str == null ? "" : str;
    }

}
