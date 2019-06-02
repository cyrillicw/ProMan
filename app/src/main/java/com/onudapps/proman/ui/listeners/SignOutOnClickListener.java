package com.onudapps.proman.ui.listeners;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.ui.activities.StartActivity;

import static android.content.Context.MODE_PRIVATE;

public class SignOutOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(StartActivity.PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(StartActivity.SIGNED_IN, false);
        editor.putString(StartActivity.PRIVATE_KEY, "");
        editor.apply();
        Intent intent = new Intent(v.getContext(), StartActivity.class);
        context.startActivity(intent);
        Repository.REPOSITORY.onSignOut();
    }
}
