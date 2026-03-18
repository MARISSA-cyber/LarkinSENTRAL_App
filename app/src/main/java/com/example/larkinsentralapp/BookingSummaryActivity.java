package com.example.larkinsentralapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BookingSummaryActivity extends AppCompatActivity {

    private TextView tvSummarySeats;
    private TextView tvPassengerCount;
    private TextView tvSummaryTotal;
    private EditText etName;
    private EditText etIc;
    private EditText etPhone;

    private ArrayList<String> selectedSeats;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);

        // Get data passed from SeatSelectionActivity
        selectedSeats = getIntent().getStringArrayListExtra("selectedSeats");
        totalPrice    = getIntent().getDoubleExtra("totalPrice", 0.0);

        // Bind views
        tvSummarySeats    = findViewById(R.id.tvSummarySeats);
        tvPassengerCount  = findViewById(R.id.tvPassengerCount);
        tvSummaryTotal    = findViewById(R.id.tvSummaryTotal);
        etName            = findViewById(R.id.etName);
        etIc              = findViewById(R.id.etIc);
        etPhone           = findViewById(R.id.etPhone);
        Button btnConfirm = findViewById(R.id.btnConfirm);
        TextView btnBack = findViewById(R.id.btnBack);

        // Populate summary
        populateSummary();

        // Listeners
        btnBack.setOnClickListener(v -> finish());

        btnConfirm.setOnClickListener(v -> confirmBooking());
    }

    // ── Fill in ticket details ─────────────────────────────────────────────
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void populateSummary() {
        // Seat list e.g. "1A, 2B, 3C"
        tvSummarySeats.setText(join(selectedSeats));

        // Passenger count
        int count = selectedSeats != null ? selectedSeats.size() : 0;
        tvPassengerCount.setText(count + " pax");

        // Total
        tvSummaryTotal.setText(String.format("RM %.2f", totalPrice));
    }


    // ── Confirm button logic ───────────────────────────────────────────────
    private void confirmBooking() {
        String name  = etName.getText().toString().trim();
        String ic    = etIc.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        // Simple validation
        if (name.isEmpty()) {
            etName.setError("Please enter your full name");
            etName.requestFocus();
            return;
        }
        if (ic.isEmpty()) {
            etIc.setError("Please enter IC / Passport number");
            etIc.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            etPhone.setError("Please enter your phone number");
            return;
        }

        // PASS DATA
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("totalPrice", totalPrice); // must not be 0
        intent.putStringArrayListExtra("selectedSeats", selectedSeats); // must not be null
        startActivity(intent);

        Toast.makeText(this, "ConfirmBooking clicked!", Toast.LENGTH_SHORT).show();
    }

    // ── Helper ────────────────────────────────────────────────────────────
    private String join(ArrayList<String> items) {
        if (items == null || items.isEmpty()) return "—";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) sb.append("  ·  ");
            sb.append(items.get(i));
        }
        return sb.toString();
    }
}
