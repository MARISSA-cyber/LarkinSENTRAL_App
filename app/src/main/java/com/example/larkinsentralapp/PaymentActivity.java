package com.example.larkinsentralapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity {

    ImageButton btnFPX, btnDuit, btnKiple, btnTng, btnCard, btnGpay, btnBack;
    Spinner spinnerBank;
    CheckBox checkAgree;
    Button btnProceed;
    Button btnCancel;

    DatabaseReference database;

    ImageButton selectedButton = null;
    String selectedMethod = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_page);

        // Firebase
        database = FirebaseDatabase.getInstance().getReference("payments");

        //find views
        btnFPX = findViewById(R.id.btnFPX);
        btnDuit = findViewById(R.id.btnDuit);
        btnKiple = findViewById(R.id.btnKiple);
        btnTng = findViewById(R.id.btnTng);
        btnCard = findViewById(R.id.btnCard);
        btnGpay = findViewById(R.id.btnGpay);
        btnBack = findViewById(R.id.btnBack);

        spinnerBank = findViewById(R.id.spinnerBank);
        checkAgree = findViewById(R.id.checkAgree);
        btnCancel = findViewById(R.id.btnCancel);

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

        // Click payment methods
        btnFPX.setOnClickListener(v -> selectPayment(btnFPX, "FPX"));
        btnDuit.setOnClickListener(v -> selectPayment(btnDuit, "DuitNow"));
        btnKiple.setOnClickListener(v -> selectPayment(btnKiple, "Kiple"));
        btnTng.setOnClickListener(v -> selectPayment(btnTng, "TNG"));
        btnCard.setOnClickListener(v -> selectPayment(btnCard, "Card"));
        btnGpay.setOnClickListener(v -> selectPayment(btnGpay, "Google Pay"));

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Proceed button
        btnProceed.setOnClickListener(v -> savePayment());

        // Cancel button
        btnCancel.setOnClickListener(v ->
                Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_SHORT).show());
    }

    // Select payment
    private void selectPayment(ImageButton button, String name) {

        // validation
        if (selectedButton != null) {
            selectedButton.setBackgroundResource(R.drawable.card_outline);
        }

        button.setBackgroundResource(R.drawable.card_selected);
        selectedButton = button;

        selectedMethod = name;

        Toast.makeText(this, name + " selected", Toast.LENGTH_SHORT).show();
    }

    // 🔥 SAVE + NAVIGATION (FIXED)
    private void savePayment() {

        String bank = spinnerBank.getSelectedItem().toString();
        String order = "TLSTR" + System.currentTimeMillis();
        String amount = "RM 18.70";

        // 🔹 validation
        if (selectedMethod.isEmpty()) {
            Toast.makeText(this, "Select payment method", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spinnerBank.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Select bank", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkAgree.isChecked()) {
            Toast.makeText(this, "Please agree to terms", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔹 Save to Firebase (clean structure)
        HashMap<String, String> map = new HashMap<>();
        map.put("order", order);
        map.put("method", selectedMethod);
        map.put("bank", bank);
        map.put("amount", amount);

        database.push().setValue(map);

        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();

        // 🔹 Go to confirmation page
        Intent intent = new Intent(this, PaymentConfirmationActivity.class);
        intent.putExtra("order", order);
        intent.putExtra("method", selectedMethod);
        intent.putExtra("bank", bank);
        intent.putExtra("amount", amount);
        startActivity(intent);
    }
}

