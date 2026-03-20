package com.example.larkinsentralapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BookingConfirmedActivity extends AppCompatActivity {

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmed);

        // ── GET INTENT DATA ──
        Intent intent = getIntent();

        String name = intent.getStringExtra("passengerName");
        ArrayList<String> selectedSeats = intent.getStringArrayListExtra("selectedSeats");

        double totalPrice = intent.getDoubleExtra("totalPrice", 0.0);

        String origin = intent.getStringExtra("origin");
        String destination = intent.getStringExtra("destination");
        String date = intent.getStringExtra("departDate");
        String time = intent.getStringExtra("time");

        // ── DEBUG (optional but useful) ──
        Toast.makeText(this,
                "Route: " + origin + " → " + destination,
                Toast.LENGTH_SHORT).show();

        // ── BOOKING REF ──
        int refNumber = (int) (Math.random() * 90000 + 10000);
        String refCode = "LRK-20260317-" + refNumber;

        // ── FIND VIEWS ──
        TextView tvRef = findViewById(R.id.tvConfirmRef);
        TextView tvName = findViewById(R.id.tvUsername);
        TextView tvSeats = findViewById(R.id.tvSummarySeats);
        TextView tvRoute = findViewById(R.id.tvConfirmRoute);
        TextView tvDateTime = findViewById(R.id.departDateText);
        TextView tvTotal = findViewById(R.id.tvSummaryTotal);

        Button btnDone = findViewById(R.id.btnDone);

        // ── SET DATA SAFELY ──
        tvRef.setText(refCode);

        tvName.setText(
                (name != null && !name.isEmpty())
                        ? name.toUpperCase()
                        : "NO NAME"
        );

        tvSeats.setText(
                (selectedSeats != null && !selectedSeats.isEmpty())
                        ? join(selectedSeats)
                        : "NO SEATS"
        );

        tvRoute.setText(
                (origin != null && destination != null)
                        ? origin + " → " + destination
                        : "NO ROUTE"
        );

        tvDateTime.setText(
                (date != null && time != null)
                        ? date + " · " + time
                        : "NO DATE / TIME"
        );

        tvTotal.setText(String.format("RM %.2f", totalPrice));

        // ── BUTTON BACK HOME ──
        btnDone.setOnClickListener(v -> {
            Intent home = new Intent(this, DashboardActivity.class);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(home);
            finish();
        });
    }

    // ── JOIN SEATS ──
    private String join(ArrayList<String> items) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(items.get(i));
        }
        return sb.toString();
    }

    // ── BACK HOME BUTTON (XML onClick) ──
    public void backhome(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}