package com.ua.erent;

import android.content.Context;
import android.widget.Toast;

import javax.inject.Inject;

/**
 * Created by Максим on 10/9/2016.
 */
public class TestService implements ITestService {

    private final Context context;

    @Inject
    public TestService(Context context) {
        this.context = context;
    }

    @Override
    public void f() {
        Toast.makeText(context, "f() was fired", Toast.LENGTH_SHORT).show();
    }
}
