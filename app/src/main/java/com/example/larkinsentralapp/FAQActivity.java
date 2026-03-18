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

public class FAQActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_faqactivity);

        // Pastikan ID di sini sama dengan ID DrawerLayout dalam activity_faq.xml
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Fungsi untuk buka Sidebar Menu (Dipanggil oleh ikon 3 garis)
    public void openDrawer(View view) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }

    // Fungsi Button Back (Ikon Revert di Header Merah)
    public void goBack(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);

        // Optional: finish current activity so it is removed from the back stack
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
        // Kita dah berada di page FAQs, cuma tutup drawer sahaja
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
}