package com.onudapps.proman.activities;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.onudapps.proman.R;

public class TaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        final TextView description = findViewById(R.id.detailed_task_description);
        final EditText descriptionEdit = findViewById(R.id.detailed_task_description_edit);
        description.setOnClickListener(v -> {
            description.setVisibility(View.INVISIBLE);
            descriptionEdit.setVisibility(View.VISIBLE);
        });
        TextView textView = findViewById(R.id.detailed_task_start_text);
        textView.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setView(R.layout.alert_date);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            TextView date = alertDialog.findViewById(R.id.date);
            date.setOnClickListener(view -> {
                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(this);
                alertDialogBuilder1.setView(R.layout.alert_date);
                AlertDialog alertDialog1 = alertDialogBuilder.create();
                alertDialog.show();
                TextView date1 = alertDialog1.findViewById(R.id.date);
                Log.e("E", "E");
            });
            alertDialog.show();
            TextView time = alertDialog.findViewById(R.id.time);
            time.setOnClickListener(view -> {
                AlertDialog.Builder timeDialogBuilder = new AlertDialog.Builder(view.getContext());
                timeDialogBuilder.setView(R.layout.time_dialog);
                AlertDialog timeDialog = timeDialogBuilder.create();
                timeDialog.show();
                TimePicker timePicker = timeDialog.findViewById(R.id.chooser_time);
                timePicker.setIs24HourView(DateFormat.is24HourFormat(alertDialog.getOwnerActivity()));
            });
        });
    }
}
