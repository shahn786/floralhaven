package com.example.floralhaven;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.floralhaven.dao.UsersDAO;
import com.example.floralhaven.database.AppDatabase;
import com.example.floralhaven.entities.Users;
import com.example.floralhaven.utilities.CommonMethods;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences appSharedPreferences;

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private TextView textViewCreateAccount;

    private UsersDAO usersDAO;

    private static final String SHARED_PREF_NAME = "app_shared_data";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_IS_ADMIN = "is_admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appSharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        if(appSharedPreferences.getString(KEY_USERNAME, null) != null) {
            Toast.makeText(this, "Already logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }

        AppDatabase appDatabase = AppDatabase.getDBInstance(this);
        usersDAO = appDatabase.usersDAO();
        validateDefaultRecords();

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewCreateAccount = findViewById(R.id.textViewCreateAccount);

        buttonLogin.setOnClickListener(v -> { login(); });
        textViewCreateAccount.setOnClickListener(v -> { startActivity(new Intent(LoginActivity.this, SignUpActivity.class)); });
    }

    private void validateDefaultRecords() {
        if(usersDAO.getUserCount() == 0) {
            usersDAO.insert(new Users("testuser1".toUpperCase(), "testuser1@gmail.com".toUpperCase(), CommonMethods.hashPassword("testuser1"), false));
            usersDAO.insert(new Users("admin2".toUpperCase(), "admin2@gmail.com".toUpperCase(), CommonMethods.hashPassword("admin2"), true));
        }
    }

    private void login() {
        String username = editTextUsername.getText().toString().trim().toUpperCase();
        String password = editTextPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        Users users = usersDAO.getUserByUsername(username);
        if (users == null) { users = usersDAO.getUserByEmail(username); }

        if (users == null) {
            Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!CommonMethods.hashPassword(password).equals(users.getPassword())) {
            Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor sharedEditor = appSharedPreferences.edit();
        sharedEditor.putInt(KEY_USER_ID, users.getUserId());
        sharedEditor.putString(KEY_USERNAME, users.getUsername());
        sharedEditor.putString(KEY_USER_EMAIL, users.getEmail());
        sharedEditor.putBoolean(KEY_USER_IS_ADMIN, users.getIsAdmin());
        sharedEditor.apply();

        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
    }
}