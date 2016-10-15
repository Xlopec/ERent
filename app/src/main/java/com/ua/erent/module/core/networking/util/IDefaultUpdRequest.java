package com.ua.erent.module.core.networking.util;

import com.ua.erent.module.core.util.IUpdateCallback;

import retrofit2.Call;

/**
 * <p>
 * Reusable class to handle requests.
 * This class has two generic params,
 * the first param means call generic type
 * and the second one - result type
 * </p>
 * Created by Максим on 6/26/2016.
 */
public abstract class IDefaultUpdRequest<C> implements IRequest {

    protected final Call<C> mCall;
    protected final IUpdateCallback callback;
    private boolean isRunning;

    public IDefaultUpdRequest(Call<C> call, IUpdateCallback callback) {
        this.mCall = call;
        this.callback = callback;
    }

    @Override
    public boolean cancel() {

        setRunning(false);

        if (!isCanceled()) {
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
