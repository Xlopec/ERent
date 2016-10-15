package com.ua.erent.module.core.presentation.mvp.model;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.dto.Credentials;
import com.ua.erent.module.core.util.IRetrieveCallback;

/**
 * Created by Максим on 10/15/2016.
 */

public interface ILoginModel {

    void login(Credentials credentials, IRetrieveCallback<Session> callback);

}
