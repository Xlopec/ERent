package com.ua.erent.module.core.presentation.mvp.view.interfaces;

import com.ua.erent.module.core.presentation.mvp.core.IBaseView;

/**
 * Created by Максим on 10/28/2016.
 */

public interface IRegisterView extends IBaseView {

    void showProgress(String message);

    void hideProgress();

    void showToast(String message);

}
