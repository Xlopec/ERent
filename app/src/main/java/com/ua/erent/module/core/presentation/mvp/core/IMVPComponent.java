package com.ua.erent.module.core.presentation.mvp.core;

/**
 * <p>
 * Each dagger component which takes part in mvp structure should
 * implement this interface
 * </p>
 * Created by Максим on 10/14/2016.
 *
 * @param <View>      view class to inject into, should be subclass of {@linkplain IBaseView}
 * @param <Presenter> subclass of {@linkplain IBasePresenter} which handles view events
 */
public interface IMVPComponent<View extends IBaseView, Presenter extends IBasePresenter<View>> {
    /**
     * Injects dependencies into specified view
     *
     * @param view view to inject into
     */
    void inject(View view);

    /**
     * @return presenter which manages view
     */
    Presenter getPresenter();

}
