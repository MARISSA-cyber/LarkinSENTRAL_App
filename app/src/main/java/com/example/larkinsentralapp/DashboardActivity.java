package com.example.larkinsentralapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link to your booking_form.xml
        setContentView(R.layout.booking_form);

        // Find the date button by its ID (make sure your XML has android:id="@+id/dateButton")
        Button dateButton = findViewById(R.id.dateButton);

        // Step 4: Add Date Picker
        dateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(
                    DashboardActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        // Update button text with selected date
                        dateButton.setText("Departing: " + dayOfMonth + "/" + (month+1) + "/" + year);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.show();
        });
    }
}
