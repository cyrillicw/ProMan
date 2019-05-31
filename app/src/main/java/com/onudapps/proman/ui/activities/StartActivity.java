package com.onudapps.proman.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    private static final String SIGNED_IN = "signed";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("ProManPreferences", MODE_PRIVATE);
        boolean signedIn = sharedPreferences.getBoolean(SIGNED_IN, false);
        if (signedIn) {
            Intent intent = new Intent(this, BoardCardsActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, SignActivity.class);
            startActivity(intent);
        }
    }
}
