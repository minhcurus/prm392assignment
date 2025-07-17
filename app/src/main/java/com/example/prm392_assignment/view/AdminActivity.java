package com.example.prm392_assignment.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_assignment.R;

public class AdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Setup buttons to Add Product, View All Products
        findViewById(R.id.btnAddProduct).setOnClickListener(v ->
                startActivity(new Intent(this, AddEditProductActivity.class))
        );

        findViewById(R.id.btnViewProducts).setOnClickListener(v ->
                startActivity(new Intent(this, ProductManagementActivity.class))
        );
    }
}
