package com.onudapps.proman;

import android.app.Application;
import android.content.SharedPreferences;
import com.onudapps.proman.data.Repository;

import static com.onudapps.proman.ui.activities.StartActivity.PREFERENCES;
import static com.onudapps.proman.ui.activities.StartActivity.SIGNED_IN;

public class ProManApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        boolean signedIn = sharedPreferences.getBoolean(SIGNED_IN, false);
        if (signedIn && !Repository.REPOSITORY.isActive()) {
            Repository.initialize(this);
        }
    }
}
