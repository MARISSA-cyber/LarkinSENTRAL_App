package com.example.larkinsentralapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class SelectOriginActivity extends AppCompatActivity {

    ListView originList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_origin);

        originList = findViewById(R.id.originList);

        String[] origins = {
                "Air Tawar — Perak (ATW)",
                "Alor Gajah — Melaka (AGH)",
                "Alor Setar — Kedah (AOR)",
                "Awana Genting Highland — Pahang (GHA)",
                "Ayer Hitam — Johor (AYH)",
                "Jelawat — Kelantan (JLT)"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                origins
        );

        originList.setAdapter(adapter);

        originList.setOnItemClickListener((parent, view, position, id) -> {
            String selected = origins[position];
            Intent intent = new Intent();
            intent.putExtra("selectedOrigin", selected);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
