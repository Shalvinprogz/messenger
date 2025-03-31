package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityMainBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupClickListeners();
    }

    private void setupClickListeners() {
        // Login button click
        binding.btnLogin.setOnClickListener(v -> {
            if (validateInputs()) {
                performLogin();
            }
        });

        // Forgot password click
        binding.tvForgotPassword.setOnClickListener(v -> {
            // Navigate to forgot password screen
            // Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            // startActivity(intent);
            Toast.makeText(LoginActivity.this, "Forgot password clicked", Toast.LENGTH_SHORT).show();
        });

        // Register click
        binding.tvRegister.setOnClickListener(v -> {
            // Navigate to registration screen
            // Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            // startActivity(intent);
            Toast.makeText(LoginActivity.this, "Register clicked", Toast.LENGTH_SHORT).show();
        });

        // Social login clicks
        binding.ivGoogle.setOnClickListener(v -> {
            // Implement Google sign-in
            Toast.makeText(LoginActivity.this, "Google sign-in clicked", Toast.LENGTH_SHORT).show();
        });

        binding.ivFacebook.setOnClickListener(v -> {
            // Implement Facebook sign-in
            Toast.makeText(LoginActivity.this, "Facebook sign-in clicked", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean validateInputs() {
        boolean isValid = true;
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        // Validate email
        if (TextUtils.isEmpty(email)) {
            binding.tilEmail.setError("Email is required");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.setError("Please enter a valid email");
            isValid = false;
        } else {
            binding.tilEmail.setError(null);
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            binding.tilPassword.setError("Password must be at least 6 characters");
            isValid = false;
        } else {
            binding.tilPassword.setError(null);
        }

        return isValid;
    }

    private void performLogin() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        // Show loading indicator
        // binding.progressBar.setVisibility(View.VISIBLE);

        // TODO: Replace with your authentication logic
        // For demonstration, just simulate successful login

        binding.btnLogin.setEnabled(false);
        binding.getRoot().postDelayed(() -> {
            binding.btnLogin.setEnabled(true);

            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }, 1500);
    }
}