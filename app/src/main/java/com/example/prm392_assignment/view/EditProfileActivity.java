package com.example.prm392_assignment.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_assignment.R;
import com.example.prm392_assignment.model.User;
import com.example.prm392_assignment.model.UserRepository;

public class EditProfileActivity extends AppCompatActivity {
    private EditText etName, etPassword;
    private EditText etEmail;

    private Button btnSave;
    private Button btnCancelEdit;

    private SharedPreferences sharedPreferences;
    private UserRepository userRepository;
    private int userId;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etName = findViewById(R.id.etEditName);
        etPassword = findViewById(R.id.etEditPassword);
        btnSave = findViewById(R.id.btnSaveProfile);
        btnCancelEdit = findViewById(R.id.btnCancelEdit);
        etEmail = findViewById(R.id.etEditEmail);


        sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        userRepository = new UserRepository(this);
        userId = sharedPreferences.getInt("user_id", -1);

        currentUser = userRepository.getUserById(userId);
        if (currentUser != null) {
            etName.setText(currentUser.getName());
            etEmail.setText(currentUser.getEmail());
        }

        btnCancelEdit.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            String newName = etName.getText().toString().trim();
            String newPassword = etPassword.getText().toString().trim();
            String newEmail = etEmail.getText().toString().trim();

            if (newName.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(this, "Name and Email cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            String updatedPassword = newPassword.isEmpty() ? currentUser.getPassword() : newPassword;
            User updatedUser = new User(userId, newName, newEmail, updatedPassword, currentUser.getRole());
            userRepository.updateUser(updatedUser);

            sharedPreferences.edit().putString("user_name", newName).putString("user_email", newEmail).apply();
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            finish(); // Quay lại ProfileActivity
        });
    }
}
