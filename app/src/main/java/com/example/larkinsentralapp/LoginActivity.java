package com.example.larkinsentralapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputEditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page); // link to your XML file

        // Firebase instance
        mAuth = FirebaseAuth.getInstance();

        // Find views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        findViewById(R.id.checkRemember);
        TextView txtForgotPassword = findViewById(R.id.txtForgotPassword);
        TextView txtSignup = findViewById(R.id.txtSignup);

        // Login button
        btnLogin.setOnClickListener(v -> {
            String email = Objects.requireNonNull(etEmail.getText()).toString().trim();
            String password = Objects.requireNonNull(etPassword.getText()).toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                            // Go to Dashboard
                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Forgot password
        txtForgotPassword.setOnClickListener(v -> {
            String email = Objects.requireNonNull(etEmail.getText()).toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Enter your email first", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Go to Sign Up
        txtSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}
