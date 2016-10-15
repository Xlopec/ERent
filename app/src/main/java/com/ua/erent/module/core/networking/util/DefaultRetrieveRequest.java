package com.ua.erent.module.core.networking.util;

/**
 * Created by Максим on 10/15/2016.
 */

import com.ua.erent.module.core.util.IRetrieveCallback;

import retrofit2.Call;

/**
 * <p>
 *     Reusable class to handle requests.
 *     This class has two generic params,
 *     the first param means call generic type
 *     and the second one - result type
 * </p>
 * Created by Максим on 6/26/2016.
 */
public abstract class DefaultRetrieveRequest< C, R > implements IRequest {

    protected final Call<C> mCall;
    protected final IRetrieveCallback<R> callback;
    private boolean isRunning;

    public DefaultRetrieveRequest(Call<C> call, IRetrieveCallback<R> callback) {
        this.mCall = call;
        this.callback = callback;
    }

    @Override
    public boolean cancel() {

        setRunning(false);

        if(!isCanceled()) {
            callback.onCancel();
            mCall.cancel();
        }

        return true;
    }

    @Override
    public boolean isCanceled() {
        return mCall.isCanceled();
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public final void execute() {
        onPreExecute();
        doExecute();
        onPostExecute();
    }

    protected abstract void doExecute();

    protected void onPreExecute() {
        setRunning(true);
        callback.onPreExecute();
    }

    protected void onPostExecute() {
        setRunning(false);
    }

    private void setRunning(boolean running) {
        this.isRunning = running;
    }

}
