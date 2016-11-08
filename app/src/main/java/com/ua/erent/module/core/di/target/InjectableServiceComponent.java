package com.ua.erent.module.core.di.target;

import android.app.Service;

/**
 * Created by Максим on 11/7/2016.
 */

public interface InjectableServiceComponent <S extends Service> {

    void inject(S service);

}
