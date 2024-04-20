package com.example.floralhaven;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class AdminPortalActivity extends AppCompatActivity {

    SharedPreferences appSharedPreferences;

    private Button buttonManageProducts, buttonManageUsers, buttonHome, buttonLogout;

    private static final String SHARED_PREF_NAME = "app_shared_data";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_IS_ADMIN = "is_admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_portal);

        appSharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String username = appSharedPreferences.getString(KEY_USERNAME, null);
        boolean isAdmin = appSharedPreferences.getBoolean(KEY_USER_IS_ADMIN, false);

        if(username == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminPortalActivity.this, LoginActivity.class));
            finish();
        }

        buttonManageProducts = findViewById(R.id.buttonManageProducts);
        buttonManageUsers = findViewById(R.id.buttonManageUsers);
        buttonHome = findViewById(R.id.buttonHome);
        buttonLogout = findViewById(R.id.buttonLogout);

        buttonManageProducts.setOnClickListener(v -> {
            startActivity(new Intent(AdminPortalActivity.this, ProductsActivity.class));
        });

        buttonManageUsers.setOnClickListener(v -> {
            startActivity(new Intent(AdminPortalActivity.this, UsersActivity.class));
        });

        buttonHome.setOnClickListener(v -> {
            startActivity(new Intent(AdminPortalActivity.this, HomeActivity.class));
            finish();
        });

        buttonLogout.setOnClickListener(v -> {
            SharedPreferences.Editor sharedEditor = appSharedPreferences.edit();
            sharedEditor.clear();
            sharedEditor.commit();
            Toast.makeText(AdminPortalActivity.this, "Logout", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminPortalActivity.this, LoginActivity.class));
            finish();
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}