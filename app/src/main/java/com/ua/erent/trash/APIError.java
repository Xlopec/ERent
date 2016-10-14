package com.ua.erent.trash;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Максим on 10/9/2016.
 */
public class APIError {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AuthErrorResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
