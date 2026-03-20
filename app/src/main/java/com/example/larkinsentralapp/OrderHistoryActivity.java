package com.example.larkinsentralapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private RecyclerView rvOrderHistory;
    private TextView tvEmpty;

    private List<BookingHistoryModel> bookingList;
    private BookingHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        btnBack = findViewById(R.id.btnBack);
        rvOrderHistory = findViewById(R.id.rvOrderHistory);
        tvEmpty = findViewById(R.id.tvEmpty);

        bookingList = new ArrayList<>();
        adapter = new BookingHistoryAdapter(bookingList);

        rvOrderHistory.setLayoutManager(new LinearLayoutManager(this));
        rvOrderHistory.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookingHistory();
    }

    private void loadBookingHistory() {
        bookingList.clear();
        bookingList.addAll(BookingHistoryManager.getBookingHistory(this));
        adapter.notifyDataSetChanged();

        Toast.makeText(this, "History count: " + bookingList.size(), Toast.LENGTH_SHORT).show();

        if (bookingList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvOrderHistory.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvOrderHistory.setVisibility(View.VISIBLE);
        }
    }
}