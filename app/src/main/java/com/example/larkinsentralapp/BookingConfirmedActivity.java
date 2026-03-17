package com.example.larkinsentralapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BookingConfirmedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmed);

        // Retrieve data passed from BookingSummaryActivity
        String              name          = getIntent().getStringExtra("passengerName");
        ArrayList<String>   selectedSeats = getIntent().getStringArrayListExtra("selectedSeats");
        double              totalPrice    = getIntent().getDoubleExtra("totalPrice", 0.0);

        // Generate a random 5-digit booking reference
        int    refNumber = (int)(Math.random() * 90000 + 10000);
        String refCode   = "LRK-20260317-" + refNumber;

        // Bind views
        TextView tvRef   = findViewById(R.id.tvConfirmRef);
        TextView tvName  = findViewById(R.id.tvConfirmName);
        TextView tvSeats = findViewById(R.id.tvConfirmSeats);
        TextView tvTotal = findViewById(R.id.tvConfirmTotal);
        Button   btnDone = findViewById(R.id.btnDone);

        // Populate
        tvRef.setText(refCode);
        tvName.setText(name != null ? name.toUpperCase() : "—");
        tvSeats.setText(selectedSeats != null ? join(selectedSeats, "  ,  ") : "");
        tvTotal.setText(String.format("RM %.2f", totalPrice));

        // Back to home: clear the entire back stack and restart seat selection
        btnDone.setOnClickListener(v -> {
            Intent intent = new Intent(this, SeatSelectionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    // ── Helper ────────────────────────────────────────────────────────────
    private String join(ArrayList<String> items, String sep) {
        if (items == null || items.isEmpty()) return "—";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) sb.append(sep);
            sb.append(items.get(i));
        }
        return sb.toString();
    }
}
