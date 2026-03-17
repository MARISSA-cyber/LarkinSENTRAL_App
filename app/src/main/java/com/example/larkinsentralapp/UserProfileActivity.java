package com.example.larkinsentralapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private TextView tvUsername, tvEmail, tvUserId, tvHistorySubtitle;

    private DatabaseReference transactionRef;
    private ArrayList<TransactionModel> transactionList;
    private TransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        // Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Initialize views
        findViewById(R.id.ivProfilePicture);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvUserId = findViewById(R.id.tvUserId);
        tvHistorySubtitle = findViewById(R.id.tvHistorySubtitle);
        RecyclerView rvTransactionHistory = findViewById(R.id.rvTransactionHistory);

        Button btnChangeAvatar = findViewById(R.id.btnChangeAvatar);
        Button btnEditProfile = findViewById(R.id.btnEditProfile);
        Button btnChangeUsername = findViewById(R.id.btnChangeUsername);
        Button btnChangePassword = findViewById(R.id.btnChangePassword);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Check login
        if (currentUser == null) {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
            finish();
            return;
        }

        // Load profile info
        loadUserProfile();

        // RecyclerView setup
        transactionList = new ArrayList<>();
        transactionAdapter = new TransactionAdapter(transactionList);
        rvTransactionHistory.setLayoutManager(new LinearLayoutManager(this));
        rvTransactionHistory.setAdapter(transactionAdapter);

        // Firebase Realtime Database reference
        transactionRef = FirebaseDatabase.getInstance()
                .getReference("transactions")
                .child(currentUser.getUid());

        // Load transaction history
        loadTransactionHistory();

        // Change Avatar
        btnChangeAvatar.setOnClickListener(v ->
                Toast.makeText(UserProfileActivity.this,
                        "Avatar upload function not connected yet",
                        Toast.LENGTH_SHORT).show()
        );

        // Update Profile
        btnEditProfile.setOnClickListener(v -> showUpdateProfileDialog());

        // Change Username
        btnChangeUsername.setOnClickListener(v -> showChangeUsernameDialog());

        // Change Password
        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        // Logout
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(UserProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @SuppressLint("SetTextI18n")
    private void loadUserProfile() {
        String email = currentUser.getEmail();
        String uid = currentUser.getUid();
        String displayName = currentUser.getDisplayName();

        tvEmail.setText(email != null ? email : "No email");
        tvUserId.setText("User ID: " + uid);

        if (displayName != null && !displayName.isEmpty()) {
            tvUsername.setText(displayName);
        } else {
            tvUsername.setText("User");
        }
    }

    private void showChangeUsernameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Username");

        final EditText input = new EditText(this);
        input.setHint("Enter new username");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newUsername = input.getText().toString().trim();

            if (newUsername.isEmpty()) {
                Toast.makeText(UserProfileActivity.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            updateUsername(newUsername);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showUpdateProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Profile");

        final EditText input = new EditText(this);
        input.setHint("Enter new username");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newUsername = input.getText().toString().trim();

            if (newUsername.isEmpty()) {
                Toast.makeText(UserProfileActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                return;
            }

            updateUsername(newUsername);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void updateUsername(String newUsername) {
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newUsername)
                .build();

        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        tvUsername.setText(newUsername);
                        Toast.makeText(UserProfileActivity.this,
                                "Username updated successfully",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UserProfileActivity.this,
                                "Failed to update username",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadTransactionHistory() {
        transactionRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                transactionList.clear();

                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        TransactionModel transaction = dataSnapshot.getValue(TransactionModel.class);
                        if (transaction != null) {
                            transactionList.add(transaction);
                        }
                    }

                    tvHistorySubtitle.setText("Your transaction history");
                } else {
                    tvHistorySubtitle.setText("No transaction history available yet");
                }

                transactionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this,
                        "Failed to load transaction history",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}