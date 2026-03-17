package com.example.larkinsentralapp;

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

public class PaymentActivity extends AppCompatActivity {

    ImageButton btnFPX, btnDuit, btnKiple, btnTng, btnCard, btnGpay, btnBack;
    Spinner spinnerBank;
    CheckBox checkAgree;
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

        // Cancel button
        btnCancel.setOnClickListener(v -> Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_SHORT).show());
    }

    // Select payment
    private void selectPayment(ImageButton button, String name) {

        // reset previous
        if (selectedButton != null) {
            selectedButton.setBackgroundResource(R.drawable.card_outline);
        }

        // highlight new
        button.setBackgroundResource(R.drawable.card_selected);
        selectedButton = button;

        selectedMethod = name;

        Toast.makeText(this, name + " selected", Toast.LENGTH_SHORT).show();
    }

}

