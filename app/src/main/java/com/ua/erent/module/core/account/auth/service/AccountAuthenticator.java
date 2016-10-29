package com.ua.erent.module.core.account.auth.service;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ua.erent.module.core.presentation.mvp.view.LoginFragment;

/**
 * <p>
 * Contains authentication logic
 * </p>
 * Created by Максим on 8/6/2016.
 */
final class AccountAuthenticator extends AbstractAccountAuthenticator {

    private final Context context;

    AccountAuthenticator(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {

        final Intent intent = new Intent(context, LoginFragment.class);
        final Bundle bundle = new Bundle();

        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken.
        final AccountManager am = AccountManager.get(context);
        final String authToken = am.peekAuthToken(account, authTokenType);

        if (authToken == null || authToken.length() == 0) {
            // If we get here, then we couldn't access the user's password - so we
            // need to re-prompt them for their credentials. We do that by creating
            // an intent to display our AuthenticatorActivity.
            final Intent intent = new Intent(context, LoginFragment.class);
            final Bundle bundle = new Bundle();

            if (null != account) {
                intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name);
            }

            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
            bundle.putParcelable(AccountManager.KEY_INTENT, intent);

            return bundle;
        }

        // If we get an authToken - we return it
        final Bundle result = new Bundle();

        result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
        result.putString(AccountManager.KEY_AUTHTOKEN, authToken);

        return result;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}
