package com.ua.erent.module.core.account.auth.domain.init.setting;

import com.ua.erent.module.core.config.IConfigModule;
import com.ua.erent.module.core.util.Initializeable;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Максим on 10/22/2016.
 */

public final class InitComponentConfigModule extends IConfigModule<Collection<? extends Initializeable>> {

    private InitComponentConfigModule() {
    }

    @Override
    public Collection<? extends Initializeable> configure() {
        return Arrays.asList(

        );
    }
}
