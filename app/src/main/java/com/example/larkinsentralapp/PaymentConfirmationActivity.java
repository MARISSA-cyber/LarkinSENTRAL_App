package com.example.larkinsentralapp;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PaymentConfirmationActivity extends AppCompatActivity {

    ImageButton btnBack;
    Button btnDone;
    TextView txtOrder, txtMethod, txtBank, txtAmount;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_confirmation_page);

        // Link XML
        btnBack = findViewById(R.id.btnBack);
        btnDone = findViewById(R.id.btnDone);
        txtOrder = findViewById(R.id.txtOrder);
        txtMethod = findViewById(R.id.txtMethod);
        txtBank = findViewById(R.id.txtBank);
        txtAmount = findViewById(R.id.txtAmount);

        // get data
        String order = getIntent().getStringExtra("order");
        String method = getIntent().getStringExtra("method");
        String bank = getIntent().getStringExtra("bank");
        String amount = getIntent().getStringExtra("amount");

        // set data to ui
        if (order != null) {
            txtOrder.setText(order);
        }

        if (method != null) {
            txtMethod.setText("Method: " + method);
        }

        if (bank != null && !bank.equals("Select Bank")) {
            txtBank.setText("Bank: " + bank);
        } else {
            txtBank.setVisibility(View.GONE);
        }

        if (amount != null) {
            txtAmount.setText(amount);
        }

        // back button
        btnBack.setOnClickListener(v -> finish());

        // done button
        btnDone.setOnClickListener(v -> {

            String orderId = txtOrder.getText().toString();
            String origin = getIntent().getStringExtra("origin");
            String destination = getIntent().getStringExtra("destination");
            String departDate = getIntent().getStringExtra("departDate");
            String time = getIntent().getStringExtra("time");
            String passengerName = getIntent().getStringExtra("passengerName");
            double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);
            ArrayList<String> selectedSeats = getIntent().getStringArrayListExtra("selectedSeats");

            BookingHistoryModel booking = new BookingHistoryModel(
                    orderId,
                    origin,
                    destination,
                    departDate,
                    totalPrice,
                    selectedSeats
            );

            BookingHistoryManager.saveBooking(PaymentConfirmationActivity.this, booking);

            Intent intent = new Intent(PaymentConfirmationActivity.this, BookingConfirmedActivity.class);
            intent.putExtra("origin", origin);
            intent.putExtra("destination", destination);
            intent.putExtra("departDate", departDate);
            intent.putExtra("time", time);
            intent.putExtra("passengerName", passengerName);
            intent.putExtra("totalPrice", totalPrice);
            intent.putStringArrayListExtra("selectedSeats", selectedSeats);
            intent.putExtra("order", orderId);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // notification
        scheduleNotification(order != null ? order : "");
    }

    // AlarmManager + BroadcastReceiver notification
    private void scheduleNotification(String order) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("order", order);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Trigger after 3 seconds
        if (alarmManager != null) {
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + 3000,
                    pendingIntent
            );
        }
    }
}