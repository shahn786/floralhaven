package com.example.floralhaven;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.floralhaven.dao.UsersDAO;
import com.example.floralhaven.database.AppDatabase;
import com.example.floralhaven.entities.Users;
import com.example.floralhaven.utilities.CommonMethods;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextNewUsername, editTextNewEmail, editTextNewPassword, editTextConfirmPassword;
    private Button buttonSignUp;

    private UsersDAO usersDAO;

    SharedPreferences appSharedPreferences;
    private static final String SHARED_PREF_NAME = "app_shared_data";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_IS_ADMIN = "is_admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        appSharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        AppDatabase appDatabase = AppDatabase.getDBInstance(this);
        usersDAO = appDatabase.usersDAO();

        editTextNewUsername = findViewById(R.id.editTextNewUsername);
        editTextNewEmail = findViewById(R.id.editTextNewEmail);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(v -> signUp());
    }

    private void signUp() {
        String newUsername = editTextNewUsername.getText().toString().trim().toUpperCase();
        String newEmail = editTextNewEmail.getText().toString().trim().toUpperCase();
        String newPassword = editTextNewPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (newUsername.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        Users existingUser = usersDAO.getUserByUsername(newUsername);
        if (existingUser != null) {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        existingUser = usersDAO.getUserByEmail(newEmail);
        if (existingUser != null) {
            Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        Users newUser = new Users(newUsername, newEmail, CommonMethods.hashPassword(newPassword), false);
        usersDAO.insert(newUser);
        newUser = usersDAO.getUserByUsername(newUsername);

        Toast.makeText(this, "Sign Up successful", Toast.LENGTH_SHORT).show();

        SharedPreferences.Editor sharedEditor = appSharedPreferences.edit();
        sharedEditor.putInt(KEY_USER_ID, newUser.getUserId());
        sharedEditor.putString(KEY_USERNAME, newUser.getUsername());
        sharedEditor.putString(KEY_USER_EMAIL, newUser.getEmail());
        sharedEditor.putBoolean(KEY_USER_IS_ADMIN, newUser.getIsAdmin());
        sharedEditor.apply();

        startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
        finish();
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
