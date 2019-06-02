package com.onudapps.proman.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import com.onudapps.proman.R;
import com.onudapps.proman.data.RemoteDataSource;
import com.onudapps.proman.data.Repository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.onudapps.proman.ui.activities.StartActivity.*;

public class SignInActivity extends AppCompatActivity {
    EditText privateKeyEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        privateKeyEdit = findViewById(R.id.sign_in_private);
        Button signIn = findViewById(R.id.sign_in);
        signIn.setOnClickListener(this::signInOnClickListener);
        // WalletUtils.generateLightNewWalletFile(text, new File(WalletUtils.getDefaultKeyDirectory()));
    }

    private void signInOnClickListener(View v) {
        final String text = privateKeyEdit.getText().toString();
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            boolean success = RemoteDataSource.signIn(text);
            data.postValue(success);
        });
        executorService.shutdown();
        data.observe(this, res -> {
            if (res) {
                SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SIGNED_IN, true);
                editor.putString(PRIVATE_KEY, text);
                editor.apply();
                Repository.initialize(this);
                Intent intent = new Intent(this, StartActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, getResources().getString(R.string.authentication_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
