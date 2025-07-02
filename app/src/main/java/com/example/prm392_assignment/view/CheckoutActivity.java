package com.example.prm392_assignment.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_assignment.R;
import com.example.prm392_assignment.model.CartItem;
import com.example.prm392_assignment.model.CartRepository;
import com.example.prm392_assignment.model.OrderRepository;
import com.example.prm392_assignment.model.Product;
import com.example.prm392_assignment.model.ProductRepository;

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private int userId;
    private List<CartItem> cartItems;
    private List<Product> allProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        SharedPreferences sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("user_id", -1);
        cartRepository = new CartRepository(this);
        productRepository = new ProductRepository(this);
        orderRepository = new OrderRepository(this);
        allProducts = productRepository.getAllProducts();
        cartItems = cartRepository.getCartItems(userId);

        RecyclerView rvCheckoutItems = findViewById(R.id.rvCheckoutItems);
        rvCheckoutItems.setLayoutManager(new LinearLayoutManager(this));
        CartAdapter cartAdapter = new CartAdapter(cartItems, allProducts, null);
        rvCheckoutItems.setAdapter(cartAdapter);

        double total = 0;
        for (CartItem item : cartItems) {
            Product p = null;
            for (Product prod : allProducts) {
                if (prod.getId() == item.getProductId()) {
                    p = prod;
                    break;
                }
            }
            if (p != null) {
                total += p.getPrice() * item.getQuantity();
            }
        }
        TextView tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvTotalAmount.setText(getString(R.string.total_amount, total));

        final double finalTotal = total;
        final List<CartItem> finalCartItems = cartItems;
        Button btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
        btnConfirmOrder.setOnClickListener(v -> {
            if (finalCartItems.isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            // Place order and clear cart
            orderRepository.placeOrder(userId, finalTotal, finalCartItems);
            cartRepository.clearCart(userId);
            Toast.makeText(this, "Order placed!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
    }
}
