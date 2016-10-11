package com.ua.erent;

import android.content.Context;
import android.widget.Toast;

import javax.inject.Inject;

/**
 * Created by Максим on 10/11/2016.
 */
public class AppServiceImp implements IAppService {

    static int i = 0;

    private final Context context;

    @Inject
    public AppServiceImp(Context context) {
        this.context = context;
        Toast.makeText(context, String.format("%d !!!", i), Toast.LENGTH_SHORT).show();
        i++;
    }

    @Override
    public void z() {
        Toast.makeText(context, "z() was fired", Toast.LENGTH_SHORT).show();
    }
}
