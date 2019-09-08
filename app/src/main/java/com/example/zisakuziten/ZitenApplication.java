package com.example.zisakuziten;

import android.app.Application;

import io.realm.Realm;

public class ZitenApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());
    }
}