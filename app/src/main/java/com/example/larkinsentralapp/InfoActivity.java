package com.example.larkinsentralapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }

    public void openFacilities(View view) {
        startActivity(new Intent(this, FacilitiesActivity.class));
    }

    public void openFAQ(View view) {
        startActivity(new Intent(this, FAQActivity.class));
    }

    public void openContact(View view) {
        startActivity(new Intent(this, ReachUsActivity.class));
    }

    // --- TAMBAHAN BARU ---

    public void openAbout(View view) {
        startActivity(new Intent(this, AboutActivity.class));
    }

    public void openFeedback(View view) {
        startActivity(new Intent(this, FeedbackActivity.class));
    }
}