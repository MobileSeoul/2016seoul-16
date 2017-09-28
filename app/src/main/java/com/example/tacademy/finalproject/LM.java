package com.example.tacademy.finalproject;

import android.util.Log;

/**
 * Created by Tacademy on 2016-10-18.
 */
public class LM {
    private static final String TAG = "MainActivity";
    private static final boolean DEBUG = true;
    public static final String SERVER = "http://52.78.90.238:8080";

    public static void v(String msg){
        if(DEBUG){
            Log.v(TAG, msg);
        }
    }
}
