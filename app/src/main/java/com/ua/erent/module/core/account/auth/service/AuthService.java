package com.ua.erent.module.core.account.auth.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public final class AuthService extends Service {

    private static AccountAuthenticator sAccountAuthenticator;

    public AuthService() {}

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {

        IBinder binder = null;

        if (intent.getAction().equals(android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT)) {
            binder = getAuthenticator().getIBinder();
        }

        return binder;
    }

    private AccountAuthenticator getAuthenticator() {

        if (null == AuthService.sAccountAuthenticator) {
            AuthService.sAccountAuthenticator = new AccountAuthenticator(this);
        }

        return AuthService.sAccountAuthenticator;
    }
}
