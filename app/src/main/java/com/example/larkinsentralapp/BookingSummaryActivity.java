package com.example.larkinsentralapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class BookingSummaryActivity extends AppCompatActivity {

    private TextView tvFrom, tvTo, tvDate, tvSummarySeats, tvPassengerCount, tvPricePerSeat, tvSummaryTotal;
    private EditText etName, etIc, etPhone;
    private Button btnConfirm;

    private ArrayList<String> selectedSeats;
    private double pricePerSeat;
    private double totalPrice;
    private String origin, destination, departDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);

        // Bind views
        tvFrom = findViewById(R.id.tvFrom);
        tvTo = findViewById(R.id.tvTo);
        tvDate = findViewById(R.id.tvDate);
        tvSummarySeats = findViewById(R.id.tvSummarySeats);
        tvPassengerCount = findViewById(R.id.tvPassengerCount);
        tvPricePerSeat = findViewById(R.id.tvPricePerSeat);
        tvSummaryTotal = findViewById(R.id.tvSummaryTotal);

        etName = findViewById(R.id.etName);
        etIc = findViewById(R.id.etIc);
        etPhone = findViewById(R.id.etPhone);
        btnConfirm = findViewById(R.id.btnConfirm);

        // Back button
        TextView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // ── Get data from SeatSelectionActivity ──
        selectedSeats = getIntent().getStringArrayListExtra("selectedSeats");
        pricePerSeat = getIntent().getDoubleExtra("pricePerSeat", 0.0);
        totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);
        origin = getIntent().getStringExtra("origin");
        destination = getIntent().getStringExtra("destination");
        departDate = getIntent().getStringExtra("departDate");

        // Populate ticket summary
        populateSummary();

        // Confirm booking click
        btnConfirm.setOnClickListener(v -> confirmBooking());
    }

    @SuppressLint("SetTextI18n")
    private void populateSummary() {
        // Origin → Destination
        tvFrom.setText(origin != null ? origin : "-");
        tvTo.setText(destination != null ? destination : "-");
        tvDate.setText(departDate != null ? departDate : "-");

        // Selected seats
        tvSummarySeats.setText(join(selectedSeats != null ? selectedSeats : new ArrayList<>()));

        // Passenger count
        int count = selectedSeats != null ? selectedSeats.size() : 0;
        tvPassengerCount.setText(count + " pax");

        // Price per seat
        tvPricePerSeat.setText(String.format("RM %.2f", pricePerSeat));

        // Total price
        tvSummaryTotal.setText(String.format("RM %.2f", totalPrice));
    }

    private void confirmBooking() {
        String name = etName.getText().toString().trim();
        String ic = etIc.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        // Validate passenger info
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
            etPhone.requestFocus();
            return;
        }

        // Proceed to payment
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("totalPrice", totalPrice);
        intent.putExtra("pricePerSeat", pricePerSeat);
        intent.putStringArrayListExtra("selectedSeats", selectedSeats);
        intent.putExtra("origin", origin);
        intent.putExtra("destination", destination);
        intent.putExtra("departDate", departDate);

        startActivity(intent);
    }

    // Helper to join seat list
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
