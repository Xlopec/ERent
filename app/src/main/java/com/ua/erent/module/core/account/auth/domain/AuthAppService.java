package com.ua.erent.module.core.account.auth.domain;

import com.ua.erent.module.core.account.auth.vo.Credentials;
import com.ua.erent.module.core.networking.service.IPacketInterceptService;
import com.ua.erent.module.core.networking.util.HTTP_CODE;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

/**
 * Created by Максим on 10/15/2016.
 */

public final class AuthAppService implements IAuthAppService {

    private final IAuthDomain domain;

    @Inject
    public AuthAppService(IPacketInterceptService interceptService, IAuthDomain domain) {

        this.domain = domain;

        interceptService.addResponseObserver(response -> {

            final HTTP_CODE httpCode = HTTP_CODE.fromHttpCode(response.code());

            if (httpCode == HTTP_CODE.UNAUTHORIZED) {
                // token expired
                logout();
            }
        });
    }

    @Override
    public void login(@NotNull Credentials credentials, @NotNull ILoginCallback callback) {
        domain.login(credentials, callback);
    }

    @Override
    public void logout() {
        domain.logout();
    }

}
