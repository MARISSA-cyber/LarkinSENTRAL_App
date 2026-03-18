package com.example.larkinsentralapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
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
import java.util.Comparator;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private ImageButton backButton, sortButton;
    private TextView tvTripRoute, tvDepartDate, tvReturnDate;
    private RecyclerView recyclerViewBuses;

    private List<Bus> busList;
    private BusAdapter adapter;

    // Trip info
    private String origin, destination, departDate, returnDate;
    private boolean isReturnTrip;
    private boolean sortByPriceAsc = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        // Views
        backButton = findViewById(R.id.backButton);
        sortButton = findViewById(R.id.btnSort);
        tvTripRoute = findViewById(R.id.tvTripRoute);
        tvDepartDate = findViewById(R.id.tvDepartDate);
        tvReturnDate = findViewById(R.id.tvReturnDate);
        recyclerViewBuses = findViewById(R.id.recyclerViewBuses);

        // Get data from booking form
        origin = getIntent().getStringExtra("origin");
        destination = getIntent().getStringExtra("destination");
        departDate = getIntent().getStringExtra("departDate");
        returnDate = getIntent().getStringExtra("returnDate");
        isReturnTrip = getIntent().getBooleanExtra("isReturnTrip", false);

        // Update trip summary
        tvTripRoute.setText(origin + " → " + destination);
        tvDepartDate.setText("Departure: " + departDate);
        if (isReturnTrip) {
            tvReturnDate.setText("Return: " + returnDate);
            tvReturnDate.setVisibility(View.VISIBLE);
        } else {
            tvReturnDate.setVisibility(View.GONE);
        }

        // RecyclerView setup
        recyclerViewBuses.setLayoutManager(new LinearLayoutManager(this));
        busList = new ArrayList<>();
        adapter = new BusAdapter(this, busList, bus -> {

            Intent intent = new Intent(SearchResultActivity.this, SeatSelectionActivity.class);

            // Pass bus info
            intent.putExtra("busName", bus.getName());
            intent.putExtra("price", bus.getPrice());
            intent.putExtra("from", bus.getFrom());
            intent.putExtra("to", bus.getTo());

            // Pass trip info also (IMPORTANT)
            intent.putExtra("origin", origin);
            intent.putExtra("destination", destination);
            intent.putExtra("departDate", departDate);
            intent.putExtra("returnDate", returnDate);
            intent.putExtra("isReturnTrip", isReturnTrip);

            startActivity(intent);
        });

        recyclerViewBuses.setAdapter(adapter);

        // Back button
        backButton.setOnClickListener(v -> finish());

        // Sort button
        sortButton.setOnClickListener(v -> {
            if (sortByPriceAsc) {
                Collections.sort(busList, Comparator.comparingInt(Bus::getPrice));
                sortByPriceAsc = false;
            } else {
                Collections.sort(busList, (b1, b2) -> b2.getPrice() - b1.getPrice());
                sortByPriceAsc = true;
            }
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Sorted by price " + (sortByPriceAsc ? "ascending" : "descending"), Toast.LENGTH_SHORT).show();
        });

        // Load buses from Firebase
        loadBusesFromFirebase();
    }

    private void loadBusesFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("buses");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                busList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Bus bus = ds.getValue(Bus.class);
                    if (bus != null) {
                        boolean originMatch = origin.equalsIgnoreCase("anywhere") || bus.getFrom().equalsIgnoreCase(origin);
                        boolean destinationMatch = destination.equalsIgnoreCase("anywhere") || bus.getTo().equalsIgnoreCase(destination);

                        if (originMatch && destinationMatch) {
                            busList.add(bus);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchResultActivity.this, "Failed to load buses", Toast.LENGTH_SHORT).show();
            }
        });
    }
}