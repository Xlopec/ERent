package com.ua.erent.module.core.presentation.mvp.model.interfaces;

import com.ua.erent.module.core.account.auth.domain.ILoginCallback;

/**
 * Created by Максим on 10/15/2016.
 */

public interface ILoginModel {

    void login(String login, String password, ILoginCallback callback);

}
