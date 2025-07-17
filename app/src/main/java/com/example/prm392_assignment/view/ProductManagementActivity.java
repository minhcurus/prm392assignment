package com.example.prm392_assignment.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_assignment.R;
import com.example.prm392_assignment.model.Product;
import com.example.prm392_assignment.model.ProductRepository;

import java.util.List;

public class ProductManagementActivity extends AppCompatActivity {

    private RecyclerView rvProductList;
    private ProductAdapter adapter;
    private ProductRepository repository;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);

        rvProductList = findViewById(R.id.rvProductList);
        rvProductList.setLayoutManager(new LinearLayoutManager(this));

        repository = new ProductRepository(this);
        productList = repository.getAllProducts();

        adapter = new ProductAdapter(productList, product -> {
            // Not needed here unless you want to "Add to Cart" in admin
        },true);

        rvProductList.setAdapter(adapter);

        // Add long click for Edit/Delete popup
        adapter.setOnItemLongClickListener((position, product) -> {
            showOptionsDialog(product);
        });
    }

    private void showOptionsDialog(Product product) {
        new AlertDialog.Builder(this)
                .setTitle("Manage Product")
                .setItems(new CharSequence[]{"Edit", "Delete"}, (dialog, which) -> {
                    if (which == 0) {
                        // Edit
                        Intent intent = new Intent(this, AddEditProductActivity.class);
                        intent.putExtra("PRODUCT_ID", product.getId());
                        startActivity(intent);
                    } else if (which == 1) {
                        // Delete
                        repository.deleteProduct(product.getId());
                        refreshList();
                    }
                }).show();
    }

    private void refreshList() {
        productList.clear();
        productList.addAll(repository.getAllProducts());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList(); // Reload after editing
    }
}
