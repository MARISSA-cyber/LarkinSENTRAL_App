package com.example.larkinsentralapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private RecyclerView rvOrderHistory;
    private TextView tvEmpty;

    private List<OrderModel> orderList;
    private OrderHistoryAdapter adapter;

    private DatabaseReference ordersRef;
    private ValueEventListener ordersListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        btnBack = findViewById(R.id.btnBack);
        rvOrderHistory = findViewById(R.id.rvOrderHistory);
        tvEmpty = findViewById(R.id.tvEmpty);

        orderList = new ArrayList<>();
        adapter = new OrderHistoryAdapter(orderList);

        rvOrderHistory.setLayoutManager(new LinearLayoutManager(this));
        rvOrderHistory.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        String userId = "user123";

        ordersRef = FirebaseDatabase.getInstance()
                .getReference("orders")
                .child(userId);

        loadOrderHistoryRealtime();
    }

    private void loadOrderHistoryRealtime() {
        ordersListener = ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    OrderModel order = dataSnapshot.getValue(OrderModel.class);
                    if (order != null) {
                        orderList.add(order);
                    }
                }

                Collections.reverse(orderList);
                adapter.notifyDataSetChanged();

                if (orderList.isEmpty()) {
                    tvEmpty.setVisibility(View.VISIBLE);
                    rvOrderHistory.setVisibility(View.GONE);
                } else {
                    tvEmpty.setVisibility(View.GONE);
                    rvOrderHistory.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderHistoryActivity.this,
                        "Failed to load orders: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ordersRef != null && ordersListener != null) {
            ordersRef.removeEventListener(ordersListener);
        }
    }
}