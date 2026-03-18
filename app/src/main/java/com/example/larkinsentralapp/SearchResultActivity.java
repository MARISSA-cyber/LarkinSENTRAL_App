package com.example.larkinsentralapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class SearchResultActivity extends AppCompatActivity {

    Button btnSelect1, btnSelect2, btnSelect3, btnSelect4;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result); // ⚠️ CHANGE THIS
        btnBack = findViewById(R.id.btnBack);

        // 🔗 Connect buttons (make sure IDs match your XML)
        btnSelect1 = findViewById(R.id.btnSelectBus1);
        btnSelect2 = findViewById(R.id.btnSelectBus2);
        btnSelect3 = findViewById(R.id.btnSelectBus3);
        btnSelect4 = findViewById(R.id.btnSelectBus4);

        // 🎯 Click → go Seat Selection
        btnSelect1.setOnClickListener(v -> goToSeat());
        btnSelect2.setOnClickListener(v -> goToSeat());
        btnSelect3.setOnClickListener(v -> goToSeat());
        btnSelect4.setOnClickListener(v -> goToSeat());
        btnBack.setOnClickListener(v -> {
            // Go back to DashboardActivity
            Intent intent = new Intent(SearchResultActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish(); // optional, to close this activity
        });

    }

    private void goToSeat() {
        Intent intent = new Intent(SearchResultActivity.this, SeatSelectionActivity.class);
        startActivity(intent);
    }
}