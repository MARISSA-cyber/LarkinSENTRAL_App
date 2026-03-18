package com.example.larkinsentralapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class DashboardActivity extends AppCompatActivity {

    EditText originInput, destinationInput;
    Button dateButton, searchTripButton;
    RadioGroup tripTypeGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_form); // link to your XML

        // Match IDs from booking_form.xml
        originInput = findViewById(R.id.originInput);
        destinationInput = findViewById(R.id.destinationInput);
        dateButton = findViewById(R.id.dateButton);
        searchTripButton = findViewById(R.id.searchTripButton);
        tripTypeGroup = findViewById(R.id.tripTypeGroup);

        // Open Origin list
        originInput.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, SelectOriginActivity.class);
            startActivityForResult(intent, 1);
        });

        // Open Destination list
        destinationInput.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, SelectDestinationActivity.class);
            startActivityForResult(intent, 2);
        });

        // Calendar pop-up
        dateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    DashboardActivity.this,
                    (view, year1, month1, dayOfMonth) -> {
                        String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                        dateButton.setText("Departing: " + selectedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        // Search Trip button → open NoTripsActivity
        searchTripButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, NoTripsActivity.class);
            startActivity(intent);
        });
    }

    // Receive selected origin/destination from list Activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1) {
                String origin = data.getStringExtra("selectedOrigin");
                originInput.setText(origin);
            } else if (requestCode == 2) {
                String destination = data.getStringExtra("selectedDestination");
                destinationInput.setText(destination);
            }
        }
    }
}
