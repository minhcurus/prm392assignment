package com.example.prm392_assignment.view;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_assignment.R;
import com.example.prm392_assignment.model.Order;
import com.example.prm392_assignment.model.OrderItemWithProduct;
import com.example.prm392_assignment.model.OrderRepository;

import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {
    private TextView tvDetailOrderId, tvDetailOrderDate, tvDetailTotalPrice;
    private OrderRepository orderRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        tvDetailOrderId = findViewById(R.id.tvDetailOrderId);
        tvDetailOrderDate = findViewById(R.id.tvDetailOrderDate);
        tvDetailTotalPrice = findViewById(R.id.tvDetailTotalPrice);

        RecyclerView rvItems = findViewById(R.id.rvOrderItems);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        int orderId = getIntent().getIntExtra("order_id", -1);

        if (orderId == -1) {
            Toast.makeText(this, "Order ID is missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        orderRepository = new OrderRepository(this);
        Order order = orderRepository.getOrderById(orderId);

        if (order != null) {
            tvDetailOrderId.setText("Order ID: " + order.getId());
            tvDetailOrderDate.setText("Date: " + order.getDate());
            tvDetailTotalPrice.setText("Total: $" + String.format("%.2f", order.getTotalPrice()));

            List<OrderItemWithProduct> orderItems = orderRepository.getOrderItemDetails(this, orderId);
            rvItems.setAdapter(new OrderItemAdapter(orderItems));
        } else {
            Toast.makeText(this, "Order not found!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}
