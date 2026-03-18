package com.example.larkinsentralapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);

        // Gunakan ID drawer_layout untuk padding system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Fungsi untuk buka Sidebar Menu
    public void openDrawer(View view) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }

    // Fungsi Button Back (Ikon Revert)
    public void goBack(View view) {
        finish();
    }

    // --- FUNGSI NAVIGATION DRAWER (Klik Menu Pindah Page) ---

    public void goToHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToFacilities(View view) {
        Intent intent = new Intent(this, FacilitiesActivity.class);
        startActivity(intent);
    }

    public void goToFaqs(View view) {
        Intent intent = new Intent(this, FAQActivity.class);
        startActivity(intent);
    }

    public void goToFeedback(View view) {
        Intent intent = new Intent(this, FeedbackActivity.class);
        startActivity(intent);
    }

    public void goToReachUs(View view) {
        Intent intent = new Intent(this, ReachUsActivity.class);
        startActivity(intent);
    }

    public void goToAbout(View view) {
        // Kita dah berada di page About, cuma tutup drawer sahaja
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}