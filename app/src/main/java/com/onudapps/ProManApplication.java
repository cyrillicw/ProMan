package com.onudapps;

import android.app.Application;
import com.onudapps.proman.data.Repository;

public class ProManApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Repository.initialize(this);
    }
}
