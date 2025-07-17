package com.example.prm392_assignment.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_assignment.R;
import com.example.prm392_assignment.model.Product;
import com.example.prm392_assignment.model.ProductRepository;
import com.example.prm392_assignment.model.CartRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Insert sample products if needed
        ProductRepository productRepo = new ProductRepository(this);
        productRepo.insertSampleProducts();
        List<Product> products = productRepo.getAllProducts();

        RecyclerView rvProducts = findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);
        CartRepository cartRepo = new CartRepository(this);
        ProductAdapter adapter = new ProductAdapter(products, product -> {
            if (userId == -1) {
                Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                return;
            }
            cartRepo.addToCart(userId, product.getId(), 1);
            Toast.makeText(this, product.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
        }, false);
        rvProducts.setAdapter(adapter);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Already on Home
                return true;
            } else if (id == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
        bottomNav.setSelectedItemId(R.id.nav_home);
    }
}
