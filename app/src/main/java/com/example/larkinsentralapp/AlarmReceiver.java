package com.example.larkinsentralapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String channelId = "PAYMENT_CHANNEL";

        // Create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Payment Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager =
                    context.getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channel);
        }

        // Get order from intent (optional)
        String order = intent.getStringExtra("order");

        String message = (order != null && !order.isEmpty())
                ? "Your booking " + order + " is confirmed"
                : "Your booking is confirmed";

        // Build notification
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("Payment Successful")
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        NotificationManagerCompat.from(context)
                .notify(1, builder.build());
    }
}