package com.example.larkinsentralapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FeedbackActivity extends AppCompatActivity {

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Untuk design yang lebih luas
        setContentView(R.layout.activity_feedback);

        // Inisialisasi Firebase
        database = FirebaseDatabase.getInstance().getReference("Feedback");

        // Set system bars padding guna ID drawer_layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Fungsi asal Firebase awak (Submit Feedback)
    public void submitFeedback(View view){
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        EditText feedbackText = findViewById(R.id.feedbackText);

        float rating = ratingBar.getRating();
        String comment = feedbackText.getText().toString();

        if(comment.isEmpty()){
            Toast.makeText(this, "Please write a feedback comment!", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = database.push().getKey();
        HashMap<String, Object> map = new HashMap<>();
        map.put("rating", rating);
        map.put("comment", comment);

        if (id != null) {
            database.child(id).setValue(map);
            Toast.makeText(this, "Feedback submitted!", Toast.LENGTH_SHORT).show();

            // Clear inputs selepas submit
            ratingBar.setRating(0);
            feedbackText.setText("");
        }
    }

    // --- FUNGSI BARU: Buka Sidebar Menu ---
    public void openDrawer(View view) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }

    // Fungsi Button Back di Header
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
        Intent intent = new Intent(this, FacilitiesActivity.class); // Ejaan sudah dibetulkan
        startActivity(intent);
    }

    public void goToFaqs(View view) {
        Intent intent = new Intent(this, FAQActivity.class);
        startActivity(intent);
    }

    public void goToFeedback(View view) {
        // Kita dah berada di page Feedback, tutup drawer sahaja
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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