package com.example.prm392_assignment.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_assignment.R;
import com.example.prm392_assignment.model.Order;
import com.example.prm392_assignment.model.OrderRepository;
import com.example.prm392_assignment.model.User;
import com.example.prm392_assignment.model.UserRepository;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private EditText etProfileName, etProfilePassword;
    private Button btnUpdateProfile, btnLogout;
    private SharedPreferences sharedPreferences;
    private UserRepository userRepository;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etProfileName = findViewById(R.id.etProfileName);
        etProfilePassword = findViewById(R.id.etProfilePassword);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnLogout = findViewById(R.id.btnLogout);
        sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        userRepository = new UserRepository(this);
        userId = sharedPreferences.getInt("user_id", -1);

        if (userId == -1) {
            // Not logged in, redirect to login
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
            return;
        }

        // Load user info
        User user = userRepository.getUserById(userId);
        if (user != null) {
            etProfileName.setText(user.getName());
        } else {
            Toast.makeText(this, "User not found. Please login again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
            return;
        }

        // Load and display order history
        OrderRepository orderRepository = new OrderRepository(this);
        List<Order> orderList = orderRepository.getOrdersByUser(userId);
        RecyclerView rvOrderHistory = findViewById(R.id.rvOrderHistory);
        rvOrderHistory.setLayoutManager(new LinearLayoutManager(this));
        OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(orderList);
        rvOrderHistory.setAdapter(orderHistoryAdapter);

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = etProfileName.getText().toString().trim();
                String newPassword = etProfilePassword.getText().toString().trim();
                if (newName.isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPassword.isEmpty()) {
                    // Only update name
                    User updatedUser = new User(userId, newName, user.getEmail(), user.getPassword());
                    userRepository.updateUser(updatedUser);
                } else {
                    // Update name and password
                    User updatedUser = new User(userId, newName, user.getEmail(), newPassword);
                    userRepository.updateUser(updatedUser);
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_name", newName);
                editor.apply();
                Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                etProfilePassword.setText("");
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
