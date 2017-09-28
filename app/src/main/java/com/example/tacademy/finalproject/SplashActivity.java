package com.example.tacademy.finalproject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashActivity extends Activity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                finish();
            }
        }; handler.sendEmptyMessageDelayed(0, 2000);

    }
}
