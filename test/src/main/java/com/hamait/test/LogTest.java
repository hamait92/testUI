package com.hamait.test;

import android.util.Log;

public class LogTest {
    private static final String TAG = "LogTest:";

    public static void d(String message){
        Log.e(TAG, ""+message);
    }

}
