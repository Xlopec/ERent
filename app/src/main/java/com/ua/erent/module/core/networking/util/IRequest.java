package com.ua.erent.module.core.networking.util;

/**
 * <p>
 *     High level abstraction for the server request.
 *     Implementations of this interface allows to monitor
 *     a state of the request, cancel it and so on
 * </p>
 * Created by Максим on 6/25/2016.
 */
public interface IRequest {
    /**
     * Cancels request. Returns true
     * if request was canceled or false
     * in another case
     * */
    boolean cancel();
    /**
     * Checks whether request was canceled
     * */
    boolean isCanceled();
    /**
     * Check whether request is running
     * */
    boolean isRunning();
    /**
     * Starts executing of the request
     * */
    void execute();

}
