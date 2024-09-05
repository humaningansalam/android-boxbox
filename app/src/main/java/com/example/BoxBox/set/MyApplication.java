package com.example.BoxBox.set;

import android.app.Application;
import android.content.SharedPreferences;

public class MyApplication extends Application {
    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("navi", MODE_PRIVATE);
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}