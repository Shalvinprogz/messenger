package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.fragments.ContainerFragment;
import com.example.myapplication.fragments.HomeFragment;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
            Toast.makeText(LoginActivity.this, "Forgot password clicked", Toast.LENGTH_SHORT).show();
        });

        // Register click
        binding.tvRegister.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Register clicked", Toast.LENGTH_SHORT).show();
        });

        // Social login clicks
        binding.ivGoogle.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Google sign-in clicked", Toast.LENGTH_SHORT).show();
        });

        binding.ivFacebook.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Facebook sign-in clicked", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean validateInputs() {
        boolean isValid = true;
        String email = Objects.requireNonNull(binding.etEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.etPassword.getText()).toString().trim();

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
        String email = Objects.requireNonNull(binding.etEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.etPassword.getText()).toString().trim();

        // Show loading indicator if you have one
        // binding.progressBar.setVisibility(View.VISIBLE);

        // Disable login button to prevent multiple clicks
        binding.btnLogin.setEnabled(false);

        // Simulate login process
        binding.getRoot().postDelayed(() -> {
            binding.btnLogin.setEnabled(true);

            // On successful login, navigate to HomeFragment
            navigateToHome();
        }, 1500);
    }

    private void navigateToHome() {
        // Hide the login form
        binding.getRoot().setVisibility(View.GONE);

        // Add the fragment container if it doesn't exist yet (first login)
        if (findViewById(R.id.fragment_container) == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, new ContainerFragment())
                    .commit();
        }

        // Navigate to home fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, HomeFragment.newInstance())
                .commit();
    }
}