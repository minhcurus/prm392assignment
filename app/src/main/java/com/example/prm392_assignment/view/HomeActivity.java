package com.example.prm392_assignment.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_assignment.R;
import com.example.prm392_assignment.model.Product;
import com.example.prm392_assignment.model.ProductRepository;
import com.example.prm392_assignment.model.CartRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private List<Product> allProducts;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Insert sample products if needed
        ProductRepository productRepo = new ProductRepository(this);
        productRepo.insertSampleProducts();
        allProducts = productRepo.getAllProducts();

        RecyclerView rvProducts = findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);
        CartRepository cartRepo = new CartRepository(this);

        // Initialize adapter with all products
        adapter = new ProductAdapter(allProducts, product -> {
            if (userId == -1) {
                Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                return;
            }
            cartRepo.addToCart(userId, product.getId(), 1);
            Toast.makeText(this, product.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
        }, false);
        rvProducts.setAdapter(adapter);

        // Setup SearchView
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterProducts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts(newText);
                return true;
            }
        });

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

    // Method to filter products based on search query
    private void filterProducts(String query) {
        List<Product> filteredList = new ArrayList<>();
        if (query == null || query.isEmpty()) {
            filteredList.addAll(allProducts);
        } else {
            String filterPattern = query.toLowerCase().trim();
            for (Product product : allProducts) {
                if (product.getName().toLowerCase().contains(filterPattern)) {
                    filteredList.add(product);
                }
            }
        }
        adapter.updateList(filteredList); // Assume ProductAdapter has an updateList method
    }
}