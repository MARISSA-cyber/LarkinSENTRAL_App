package com.example.larkinsentralapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class SelectDestinationActivity extends AppCompatActivity {

    ListView destinationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_destination);

        destinationList = findViewById(R.id.destinationList);

        String[] destinations = {
                "Alor Setar — Kedah (AOR)",
                "Pasir Putih — Kelantan (PTH)",
                "Butterworth — Pulau Pinang (BTW)",
                "Changlun — Kedah (CLN)",
                "JB - Larkin — Johor (LAR)",
                "Jerteh — Terengganu (JTH)"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                destinations
        );

        destinationList.setAdapter(adapter);

        destinationList.setOnItemClickListener((parent, view, position, id) -> {
            String selected = destinations[position];
            Intent intent = new Intent();
            intent.putExtra("selectedDestination", selected);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
