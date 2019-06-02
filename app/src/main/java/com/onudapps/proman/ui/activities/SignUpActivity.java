package com.onudapps.proman.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import com.onudapps.proman.R;
import com.onudapps.proman.data.RemoteDataSource;
import com.onudapps.proman.data.Repository;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.onudapps.proman.ui.activities.StartActivity.*;

public class SignUpActivity extends AppCompatActivity {
    private EditText privateKeyEdit;
    private EditText nickNameEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        privateKeyEdit = findViewById(R.id.sign_up_private);
        nickNameEdit = findViewById(R.id.sign_up_nickname);
        Button signUpButton = findViewById(R.id.sign_up);
        signUpButton.setOnClickListener(this::signUpOnClickListener);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(privateKeyEdit, InputMethodManager.SHOW_IMPLICIT);
    }

    private void signUpOnClickListener(View v) {
        final String privateKey = privateKeyEdit.getText().toString();
        final String nickName = nickNameEdit.getText().toString();
        if (!nickName.equals("") && !privateKey.equals("")) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(privateKeyEdit.getWindowToken(), 0);
            MutableLiveData<Boolean> data = new MutableLiveData<>();
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                TransactionReceipt tx = RemoteDataSource.signUp(privateKey, nickName);
                if (tx != null) {
                    int res = Integer.parseInt(tx.getLogs().get(0).getData().substring(2));
                    data.postValue(res > 0);
                } else {
                    data.postValue(false);
                }
            });
            executorService.shutdown();
            data.observe(this, res -> {
                if (res) {
                    SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SIGNED_IN, true);
                    editor.putString(PRIVATE_KEY, privateKey);
                    editor.apply();
                    Repository.initialize(this);
                    Intent intent = new Intent(this, StartActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, getResources().getString(R.string.sign_up_failure), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
