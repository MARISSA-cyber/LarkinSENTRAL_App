package com.example.larkinsentralapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.InputType;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private TextView tvUsername, tvEmail, tvUserId;
    private ImageView ivProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvUserId = findViewById(R.id.tvUserId);

        Button btnChangeAvatar = findViewById(R.id.btnChangeAvatar);
        Button btnEditProfile = findViewById(R.id.btnEditProfile);
        Button btnChangeUsername = findViewById(R.id.btnChangeUsername);
        Button btnChangePassword = findViewById(R.id.btnChangePassword);
        Button btnLogout = findViewById(R.id.btnLogout);

        if (currentUser == null) {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
            finish();
            return;
        }

        loadUserProfile();
        loadAvatarFromDatabase();

        btnChangeAvatar.setOnClickListener(v -> showAvatarSelectionDialog());
        btnEditProfile.setOnClickListener(v -> showUpdateProfileDialog());
        btnChangeUsername.setOnClickListener(v -> showChangeUsernameDialog());

        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

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

    private void showAvatarSelectionDialog() {
        String[] options = {"Avatar 1", "Avatar 2", "Avatar 3"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Avatar");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                updateAvatar("avatar1");
            } else if (which == 1) {
                updateAvatar("avatar2");
            } else if (which == 2) {
                updateAvatar("avatar3");
            }
        });
        builder.show();
    }

    private void updateAvatar(String avatarName) {
        int avatarResId;

        if ("avatar1".equals(avatarName)) {
            avatarResId = R.drawable.avatar1;
        } else if ("avatar2".equals(avatarName)) {
            avatarResId = R.drawable.avatar2;
        } else if ("avatar3".equals(avatarName)) {
            avatarResId = R.drawable.avatar3;
        } else {
            avatarResId = R.drawable.avatar1; // default
        }

        // 显示头像
        ivProfilePicture.setImageResource(avatarResId);

        // 存进 database
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(currentUser.getUid());

        userRef.child("avatar").setValue(avatarName)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Avatar updated successfully", Toast.LENGTH_SHORT).show();

                    // 更新 Firebase Auth（可选）
                    UserProfileChangeRequest profileUpdates =
                            new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(Uri.parse("android.resource://" + getPackageName() + "/drawable/" + avatarName))
                                    .build();

                    currentUser.updateProfile(profileUpdates);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to update avatar", Toast.LENGTH_SHORT).show()
                );
    }
    private void loadAvatarFromDatabase() {
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(currentUser.getUid());

        userRef.child("avatar").get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String avatarName = snapshot.getValue(String.class);

                if ("avatar1".equals(avatarName)) {
                    ivProfilePicture.setImageResource(R.drawable.avatar1);
                } else if ("avatar2".equals(avatarName)) {
                    ivProfilePicture.setImageResource(R.drawable.avatar2);
                } else if ("avatar3".equals(avatarName)) {
                    ivProfilePicture.setImageResource(R.drawable.avatar3);
                } else {
                    ivProfilePicture.setImageResource(R.drawable.avatar1);
                }
            } else {
                ivProfilePicture.setImageResource(R.drawable.avatar1);
            }
        }).addOnFailureListener(e ->
                ivProfilePicture.setImageResource(R.drawable.avatar1)
        );
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
                        DatabaseReference userRef = FirebaseDatabase.getInstance()
                                .getReference("users")
                                .child(currentUser.getUid());

                        userRef.child("username").setValue(newUsername);

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
}