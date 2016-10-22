package com.ua.erent.module.core.networking.util;

/**
 * <p>
 *     This enum represents http response codes
 * </p>
 * Created by Максим on 10/21/2016.
 */

public enum HTTP_CODE {

    UNAUTHORIZED(401);

    private final int httpCode;

    HTTP_CODE(int httpCode) {
        this.httpCode = httpCode;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public static HTTP_CODE fromHttpCode(int httpCode) {

        for (final HTTP_CODE code : HTTP_CODE.values()) {
            if (code.getHttpCode() == httpCode) return code;
        }

        return null;
    }

}
