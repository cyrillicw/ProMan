package com.onudapps.proman.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import com.onudapps.proman.BuildConfig;
import com.onudapps.proman.R;
import com.onudapps.proman.data.Repository;

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
        LiveData<Boolean> signInData = Repository.REPOSITORY.signIn(text);
        signInData.observe(this, res -> {
            if (res) {
                SharedPreferences sharedPreferences = getSharedPreferences(BuildConfig.preferences, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("PrivateKey", text);
            }
            else {
                Toast.makeText(this, getResources().getString(R.string.authentication_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
