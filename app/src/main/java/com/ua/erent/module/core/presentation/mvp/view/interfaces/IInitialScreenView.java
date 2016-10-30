package com.ua.erent.module.core.presentation.mvp.view.interfaces;

import com.ua.erent.module.core.presentation.mvp.core.IBaseView;

/**
 * Created by Максим on 10/28/2016.
 */

public interface IInitialScreenView extends IBaseView {

    interface NavigationListener {

        void onCreateAccount();

        void onRestorePassword();

        void onLogin();

    }

}
