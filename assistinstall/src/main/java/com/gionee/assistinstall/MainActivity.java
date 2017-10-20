package com.gionee.assistinstall;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity
        extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this,InService.class));
        new Thread(){
            @Override
            public void run() {
                SystemClock.sleep(5000);
                finish();
            }
        }.start();

    }
}
