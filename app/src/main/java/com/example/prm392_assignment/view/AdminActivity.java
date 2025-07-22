package com.example.prm392_assignment.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_assignment.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_add_product) {
                startActivity(new Intent(this, AddEditProductActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_manage_products) {
                startActivity(new Intent(this, ProductManagementActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_manage_users) {
                startActivity(new Intent(this, UserManagementActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_manage_orders) {
                startActivity(new Intent(this, OrderManagementActivity.class));
                return true;
            }
            return false;
        });
    }
}
