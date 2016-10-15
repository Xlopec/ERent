package com.ua.erent.module.core.account.auth.domain.api;

import com.ua.erent.module.core.account.auth.bo.Session;
import com.ua.erent.module.core.account.auth.dto.Credentials;
import com.ua.erent.module.core.networking.util.IRequest;
import com.ua.erent.module.core.util.IRetrieveCallback;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Максим on 10/15/2016.
 */

public interface ISessionProvider {

    IRequest authAsync(@NotNull Credentials credentials, @NotNull IRetrieveCallback<Session> callback);

}
