package com.ua.erent.module.core.app;

import android.app.Application;

import com.ua.erent.BuildConfig;
import com.ua.erent.R;

import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

/**
 * <p>
 *     Main class, which represents the whole
 *     app
 * </p>
 * Created by Максим on 10/9/2016.
 */
@ReportsCrashes(
        mailTo = BuildConfig.APP_CRASH_MAILTO,
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.err_app_crash
)
public final class AndroidApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // initializes configuration composer
        AppConfigComposer.initialize(this);
    }
}
