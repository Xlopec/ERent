package com.ua.erent;

import android.content.Context;
import android.widget.Toast;

import javax.inject.Inject;

/**
 * Created by Максим on 10/9/2016.
 */
public class TestService implements ITestService {

    static int i = 0;

    private final Context context;

    @Inject
    public TestService(Context context) {
        this.context = context;
        Toast.makeText(context, String.format("%d instances were created", i), Toast.LENGTH_SHORT).show();
        i++;
    }

    @Override
    public void f() {
        Toast.makeText(context, "f() was fired", Toast.LENGTH_SHORT).show();
    }
}
