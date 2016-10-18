package com.ua.erent.module.core.presentation.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.ua.erent.R;
import com.ua.erent.module.core.presentation.mvp.core.IBaseView;

import org.jetbrains.annotations.NotNull;

public class NextActivity extends AppCompatActivity implements IBaseView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        Button button = (Button) findViewById(R.id.back);

        button.setOnClickListener((v) -> {
            NextActivity.this.startActivity(new Intent(NextActivity.this, LoginActivity.class));
           // NextActivity.this.finish();
        });
        System.gc();
        /*AppComponent provider = Injector.injector().getComponent(this, AppComponent.class);

        IAppService service = provider.getAppService();

        service.z();*/
    }

    @NotNull
    @Override
    public Context getContext() {
        return null;
    }

    @NotNull
    @Override
    public Context getApplicationContext() {
        return null;
    }
}
