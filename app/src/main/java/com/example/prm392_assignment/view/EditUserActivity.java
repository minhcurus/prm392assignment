package com.example.prm392_assignment.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_assignment.R;
import com.example.prm392_assignment.model.User;
import com.example.prm392_assignment.model.UserRepository;

public class EditUserActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPassword;
    private Button btnSave;
    private int userId;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        etName = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etUserEmail);
        etPassword = findViewById(R.id.etUserPassword);
        btnSave = findViewById(R.id.btnSaveUser);
        userRepository = new UserRepository(this);

        userId = getIntent().getIntExtra("USER_ID", -1);

        if (userId != -1) {
            User user = userRepository.getUserById(userId);
            if (user != null) {
                etName.setText(user.getName());
                etEmail.setText(user.getEmail());
                etPassword.setText(user.getPassword());
            }
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            User updatedUser = new User(userId, name, email, password, "User");
            userRepository.updateUserAdmin(updatedUser);

            Toast.makeText(this, "User updated", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();

        });
    }
}
