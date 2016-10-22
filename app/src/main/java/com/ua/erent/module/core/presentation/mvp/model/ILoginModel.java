package com.ua.erent.module.core.presentation.mvp.model;

import com.ua.erent.module.core.account.auth.domain.ILoginCallback;
import com.ua.erent.module.core.account.auth.vo.Credentials;

/**
 * Created by Максим on 10/15/2016.
 */

public interface ILoginModel {

    void login(Credentials credentials, ILoginCallback callback);

}
