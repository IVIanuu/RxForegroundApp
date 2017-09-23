package com.ivianuu.rxforegroundapp.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ivianuu.rxforegroundapp.RxForegroundApp;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RxForegroundApp.observeForegroundApp(this, 100, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d("ForegroundApp", "the current foreground app is " + s);
                    }
                });
    }
}
