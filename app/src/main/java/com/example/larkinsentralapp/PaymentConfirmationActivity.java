package com.example.larkinsentralapp;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class PaymentConfirmationActivity extends AppCompatActivity {

    ImageButton btnBack;
    Button btnDone;
    TextView txtOrder, txtMethod, txtBank, txtAmount;

    @SuppressLint("MissingInflatedId")
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

        //set data to ui
        if (order != null) {
            txtOrder.setText(order);
        }

        if (method != null) {
            txtMethod.setText(method);
        }

        if (bank != null) {
            txtBank.setText(bank);
        }

        if (amount != null) {
            txtAmount.setText(amount);
        }

        // back button
        btnBack.setOnClickListener(v -> finish());

        //done button
        btnDone.setOnClickListener(v -> {
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
            finish();
        });

        // 🔹 Show notification
        showNotification(order);
    }


    // function to show notification
    private void showNotification(String order) {
        String channelId = "payment_channel";
        String channelName = "Payment Notification";

        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // function to show notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            manager.createNotificationChannel(channel);
        }

        // message text
        String message;
        if (order != null && !order.isEmpty()) {
            message = "Your booking " + order + " is confirmed";
        } else {
            message = "Your booking is confirmed";
        }

        // build notification
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("Payment Successful")
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //Show notification
        manager.notify(1, builder.build());
    }
}
