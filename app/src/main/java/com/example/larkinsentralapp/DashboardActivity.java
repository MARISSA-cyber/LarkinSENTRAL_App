package com.example.larkinsentralapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_form);

        // Date Button
        Button dateButton = findViewById(R.id.dateButton);

        dateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(
                    DashboardActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        dateButton.setText("Departing: " + dayOfMonth + "/" + (month+1) + "/" + year);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.show();
        });

        Button profileButton = findViewById(R.id.profileButton);

        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        Button btnSearch = findViewById(R.id.searchTripButton);

        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, SearchResultActivity.class);
            startActivity(intent);
        });

        
    }
}