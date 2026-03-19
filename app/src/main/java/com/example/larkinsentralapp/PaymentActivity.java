package com.example.larkinsentralapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity {

    ImageButton btnFPX, btnDuit, btnKiple, btnTng, btnCard, btnGpay, btnBack;
    Spinner spinnerBank;
    CheckBox checkAgree;
    Button btnProceed, btnCancel;

    DatabaseReference database;

    ImageButton selectedButton = null;
    String selectedMethod = null;

    ArrayList<String> seats;
    double totalPrice;

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_page);

        // Firebase
        database = FirebaseDatabase.getInstance().getReference("payments");

        // GET DATA
        totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);
        seats = getIntent().getStringArrayListExtra("selectedSeats");

        // Find views
        btnFPX = findViewById(R.id.btnFPX);
        btnDuit = findViewById(R.id.btnDuit);
        btnKiple = findViewById(R.id.btnKiple);
        btnTng = findViewById(R.id.btnTng);
        btnCard = findViewById(R.id.btnCard);
        btnGpay = findViewById(R.id.btnGpay);
        btnBack = findViewById(R.id.btnBack);

        spinnerBank = findViewById(R.id.spinnerBank);
        checkAgree = findViewById(R.id.checkAgree);
        btnProceed = findViewById(R.id.btnProceed);
        btnCancel = findViewById(R.id.btnCancel);

        TextView tvAmount = findViewById(R.id.tvAmount);
        tvAmount.setText("Order Amount: RM " + String.format("%.2f", totalPrice));

        // Spinner data
        String[] banks = {
                "Select Bank",
                "Maybank",
                "CIMB",
                "Public Bank",
                "RHB",
                "Hong Leong"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                banks
        );
        spinnerBank.setAdapter(adapter);

        // Payment clicks
        btnFPX.setOnClickListener(v -> selectPayment(btnFPX, "FPX"));
        btnDuit.setOnClickListener(v -> selectPayment(btnDuit, "DuitNow"));
        btnKiple.setOnClickListener(v -> selectPayment(btnKiple, "Kiple"));
        btnTng.setOnClickListener(v -> selectPayment(btnTng, "TNG"));
        btnCard.setOnClickListener(v -> selectPayment(btnCard, "Card"));
        btnGpay.setOnClickListener(v -> selectPayment(btnGpay, "Google Pay"));

        // Back
        btnBack.setOnClickListener(v -> finish());

        // Proceed
        btnProceed.setOnClickListener(v -> savePayment());

        // Cancel
        btnCancel.setOnClickListener(v ->
                Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_SHORT).show());
    }

    // SELECT PAYMENT (FIXED)
    private void selectPayment(ImageButton button, String name) {

        if (selectedButton != null) {
            selectedButton.setBackgroundResource(R.drawable.card_outline);
        }

        button.setBackgroundResource(R.drawable.card_selected);
        selectedButton = button;

        selectedMethod = name;

        Toast.makeText(this, "Selected: " + selectedMethod, Toast.LENGTH_SHORT).show();
    }

    // SAVE PAYMENT (FIXED)
    private void savePayment() {

        String order = "TLSTR" + System.currentTimeMillis();
        @SuppressLint("DefaultLocale")
        String amount = "RM " + String.format("%.2f", totalPrice);

        // CHECK PAYMENT METHOD
        if (selectedMethod == null) {
            Toast.makeText(this, "Select payment method", Toast.LENGTH_SHORT).show();
            return;
        }

        // ONLY FPX NEED BANK
        if (selectedMethod.equals("FPX") && spinnerBank.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Select bank", Toast.LENGTH_SHORT).show();
            return;
        }

        // TERMS CHECK
        if (!checkAgree.isChecked()) {
            Toast.makeText(this, "Please agree to terms", Toast.LENGTH_SHORT).show();
            return;
        }

        String bank = spinnerBank.getSelectedItem().toString();

        // Save to Firebase
        HashMap<String, String> map = new HashMap<>();
        map.put("order", order);
        map.put("method", selectedMethod);
        map.put("bank", bank);
        map.put("amount", amount);

        database.push().setValue(map);

        btnCancel.setOnClickListener(v -> {
            Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_SHORT).show();
            finish();
        });

        // Go next
        Intent intent = new Intent(this, PaymentConfirmationActivity.class);

        intent.putExtra("totalPrice", totalPrice);
        intent.putStringArrayListExtra("selectedSeats", seats);
        intent.putExtra("order", order);
        intent.putExtra("method", selectedMethod);
        intent.putExtra("bank", bank);
        intent.putExtra("amount", amount);

        startActivity(intent);
    }
}