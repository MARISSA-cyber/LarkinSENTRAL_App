package com.example.larkinsentralapp;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookingHistoryManager {

    private static final String PREF_NAME = "booking_history_pref";
    private static final String KEY_HISTORY = "booking_history";

    public static void saveBooking(Context context, BookingHistoryModel booking) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            String existingData = prefs.getString(KEY_HISTORY, "[]");

            JSONArray jsonArray = new JSONArray(existingData);

            JSONObject obj = new JSONObject();
            obj.put("orderId", booking.getOrderId());
            obj.put("origin", booking.getOrigin());
            obj.put("destination", booking.getDestination());
            obj.put("departDate", booking.getDepartDate());
            obj.put("totalPrice", booking.getTotalPrice());

            JSONArray seatsArray = new JSONArray();
            if (booking.getSelectedSeats() != null) {
                for (String seat : booking.getSelectedSeats()) {
                    seatsArray.put(seat);
                }
            }
            obj.put("selectedSeats", seatsArray);

            jsonArray.put(obj);

            prefs.edit().putString(KEY_HISTORY, jsonArray.toString()).apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<BookingHistoryModel> getBookingHistory(Context context) {
        List<BookingHistoryModel> list = new ArrayList<>();

        try {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            String data = prefs.getString(KEY_HISTORY, "[]");

            JSONArray jsonArray = new JSONArray(data);

            for (int i = jsonArray.length() - 1; i >= 0; i--) {
                JSONObject obj = jsonArray.getJSONObject(i);

                String orderId = obj.optString("orderId");
                String origin = obj.optString("origin");
                String destination = obj.optString("destination");
                String departDate = obj.optString("departDate");
                String time = obj.optString("time");
                String passengerName = obj.optString("passengerName");
                double totalPrice = obj.optDouble("totalPrice");

                ArrayList<String> seats = new ArrayList<>();
                JSONArray seatsArray = obj.optJSONArray("selectedSeats");
                if (seatsArray != null) {
                    for (int j = 0; j < seatsArray.length(); j++) {
                        seats.add(seatsArray.getString(j));
                    }
                }

                list.add(new BookingHistoryModel(
                        orderId, origin, destination, departDate, totalPrice, seats
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void clearHistory(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_HISTORY).apply();
    }
}
