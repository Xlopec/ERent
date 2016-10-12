package com.ua.erent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.ua.erent.module.core.di.Injector;
import com.ua.erent.module.core.app.component.AppComponent;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        Button button = (Button) findViewById(R.id.back);

        button.setOnClickListener((v) -> {
            NextActivity.this.startActivity(new Intent(NextActivity.this, MainActivity.class));
           // NextActivity.this.finish();
        });

        AppComponent provider = Injector.injector().getComponent(AppComponent.class);

        IAppService service = provider.getAppService();

        service.z();
    }
}
