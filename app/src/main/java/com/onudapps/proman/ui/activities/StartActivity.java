package com.onudapps.proman.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.onudapps.proman.data.Repository;

public class StartActivity extends AppCompatActivity {
    public static final String SIGNED_IN = "Signed";
    public static final String PREFERENCES = "ProManPreferences";
    public static final String PRIVATE_KEY = "PrivateKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        boolean signedIn = sharedPreferences.getBoolean(SIGNED_IN, false);
        if (signedIn) {
            Repository.initialize(this);
            Intent intent = new Intent(this, BoardCardsActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, SignActivity.class);
            startActivity(intent);
        }
    }
}
