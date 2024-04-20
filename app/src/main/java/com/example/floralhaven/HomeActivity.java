package com.example.floralhaven;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences appSharedPreferences;

    private TextView textViewWelcome;
    private Button buttonViewCart, buttonBrowseFlowers, buttonOrderHistory, buttonAdminPortal, buttonLogout;

    private static final String SHARED_PREF_NAME = "app_shared_data";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_IS_ADMIN = "is_admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        appSharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String username = appSharedPreferences.getString(KEY_USERNAME, null);
        boolean isAdmin = appSharedPreferences.getBoolean(KEY_USER_IS_ADMIN, false);

        if(username == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }

        textViewWelcome = findViewById(R.id.textViewWelcome);
        buttonViewCart = findViewById(R.id.buttonViewCart);
        buttonBrowseFlowers = findViewById(R.id.buttonBrowseFlowers);
        buttonOrderHistory = findViewById(R.id.buttonOrderHistory);
        buttonAdminPortal = findViewById(R.id.buttonAdminPortal);
        buttonLogout = findViewById(R.id.buttonLogout);

        textViewWelcome.setText("Welcome, " + username + "!");

        buttonViewCart.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, CartActivity.class)));
        buttonBrowseFlowers.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, FlowersActivity.class)));
        buttonOrderHistory.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, OrderActivity.class)));
        buttonAdminPortal.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, AdminPortalActivity.class)));

        if (isAdmin) { buttonAdminPortal.setVisibility(View.VISIBLE); }
        else { buttonAdminPortal.setVisibility(View.INVISIBLE); }

        buttonLogout.setOnClickListener(v -> {
            SharedPreferences.Editor sharedEditor = appSharedPreferences.edit();
            sharedEditor.clear();
            sharedEditor.commit();
            Toast.makeText(HomeActivity.this, "Logout", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        });
    }
}