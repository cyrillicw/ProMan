package com.onudapps;

import android.app.Application;

public class ProManApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Repository.initialize(this);
    }
}
