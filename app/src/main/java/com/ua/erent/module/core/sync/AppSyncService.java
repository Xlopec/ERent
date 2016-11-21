package com.ua.erent.module.core.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.ContentResolver;
import android.os.Bundle;

import com.ua.erent.module.core.app.Constant;

/**
 * Created by Максим on 11/21/2016.
 */

public final class AppSyncService implements IAppSyncService {

    private final AccountManager accountManager;

    public AppSyncService(Application application) {
        accountManager = AccountManager.get(application);
    }

    @Override
    public void start() {

        startSyncForAccounts(accountManager.getAccounts());
        accountManager.addOnAccountsUpdatedListener(accounts -> {

            if (!(accounts == null || accounts.length == 0)) {
                startSyncForAccounts(accounts);
            }
        }, null, true);
    }

    private void startSyncForAccounts(Account[] accounts) {

        for (final Account account : accounts) {

            if (account.type.equals(Constant.ACCOUNT_TYPE)
                    && !ContentResolver.isSyncActive(account, Constant.ACCOUNT_AUTHORITY)
                    && !ContentResolver.isSyncPending(account, Constant.ACCOUNT_AUTHORITY)) {

                ContentResolver.setSyncAutomatically(account, Constant.ACCOUNT_AUTHORITY, true);
                ContentResolver.addPeriodicSync(account, Constant.ACCOUNT_AUTHORITY,
                        Bundle.EMPTY, Constant.SYNC_PERIOD);
            }
        }
    }

}
