package com.example.prm392_assignment.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    private TextView tvName, tvEmail;

    private Button btnEditProfile, btnLogout;
    private RecyclerView rvOrderHistory;

    private SharedPreferences sharedPreferences;
    private UserRepository userRepository;
    private OrderRepository orderRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tvProfileName);
        tvEmail = findViewById(R.id.tvProfileEmail);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);
        rvOrderHistory = findViewById(R.id.rvOrderHistory);

        sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);

        if (userId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        userRepository = new UserRepository(this);
        orderRepository = new OrderRepository(this);

        User user = userRepository.getUserById(userId);
        if (user != null) {
            tvName.setText(user.getName());
            tvEmail.setText(user.getEmail());
        }

        List<Order> orderList = orderRepository.getOrdersByUser(userId);
        rvOrderHistory.setLayoutManager(new LinearLayoutManager(this));
        rvOrderHistory.setAdapter(new OrderHistoryAdapter(orderList));

        btnEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            sharedPreferences.edit().clear().apply();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        int userId = sharedPreferences.getInt("user_id", -1);
        if (userId != -1) {
            User user = userRepository.getUserById(userId);
            if (user != null) {
                tvName.setText(user.getName());
                tvEmail.setText(user.getEmail());
            }
        }
    }

}
