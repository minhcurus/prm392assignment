package com.example.prm392_assignment.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_assignment.R;
import com.example.prm392_assignment.model.CartItem;
import com.example.prm392_assignment.model.CartRepository;
import com.example.prm392_assignment.model.Product;
import com.example.prm392_assignment.model.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private int userId;
    private List<CartItem> cartItems;
    private List<Product> allProducts;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        SharedPreferences sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("user_id", -1);
        cartRepository = new CartRepository(this);
        productRepository = new ProductRepository(this);
        allProducts = productRepository.getAllProducts();
        cartItems = cartRepository.getCartItems(userId);

        RecyclerView rvCartItems = findViewById(R.id.rvCartItems);
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(cartItems, allProducts, new CartAdapter.CartActionListener() {
            @Override
            public void onQuantityChanged(CartItem item, int newQty) {
                cartRepository.updateCartItem(item.getId(), newQty);
                Toast.makeText(CartActivity.this, "Quantity updated", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRemove(CartItem item) {
                cartRepository.removeCartItem(item.getId());
                cartItems.remove(item);
                cartAdapter.notifyDataSetChanged();
                Toast.makeText(CartActivity.this, "Item removed", Toast.LENGTH_SHORT).show();
            }
        });
        rvCartItems.setAdapter(cartAdapter);

        Button btnCheckout = findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(this, CheckoutActivity.class));
        });
    }
}
