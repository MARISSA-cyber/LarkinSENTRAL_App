package com.example.larkinsentralapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BookingSummaryActivity extends AppCompatActivity {

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);

        // ── GET DATA FROM PREVIOUS PAGE ──
        String origin = getIntent().getStringExtra("origin");
        String destination = getIntent().getStringExtra("destination");
        String date = getIntent().getStringExtra("departDate");
        String departureTime = getIntent().getStringExtra("time");

        ArrayList<String> seats = getIntent().getStringArrayListExtra("selectedSeats");

        double pricePerSeat = getIntent().getDoubleExtra("pricePerSeat", 0.0);

        // ── CALCULATION ──
        int passengerCount = (seats != null) ? seats.size() : 0;
        double totalPrice = pricePerSeat * passengerCount;

        getIntent().getDoubleExtra("pricePerSeat", 0.0);
        // ── BIND VIEWS ──
        TextView tvFrom = findViewById(R.id.originInput);
        TextView tvTo = findViewById(R.id.destinationInput);
        TextView tvDate = findViewById(R.id.departDateText);
        TextView tvSeats = findViewById(R.id.tvSummarySeats);
        TextView tvPassenger = findViewById(R.id.tvPassengerCount);
        TextView tvPrice = findViewById(R.id.busPrice);
        TextView tvTotal = findViewById(R.id.tvSummaryTotal);
        TextView tvDeparture = findViewById(R.id.busDeparture);

        // ── SET DATA ──
        tvFrom.setText(origin != null ? origin : "—");
        tvTo.setText(destination != null ? destination : "—");
        tvDate.setText(date != null ? date : "—");
        tvDeparture.setText(departureTime != null ? departureTime : "—");

        tvSeats.setText(seats != null ? join(seats) : "—");
        tvPassenger.setText(String.valueOf(passengerCount));

        tvPrice.setText(String.format("RM %.2f", pricePerSeat));
        tvTotal.setText(String.format("RM %.2f", totalPrice));
    }

    // ── HELPER (same style as your confirm page) ──
    private String join(ArrayList<String> items) {
        if (items == null || items.isEmpty()) return "—";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(items.get(i));
        }
        return sb.toString();
    }
    public void payment (View v) {
        Intent intent = new Intent(BookingSummaryActivity.this,PaymentActivity.class);
        startActivity(intent);
    }
}