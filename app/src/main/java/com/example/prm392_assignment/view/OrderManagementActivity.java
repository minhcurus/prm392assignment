package com.example.prm392_assignment.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_assignment.R;
import com.example.prm392_assignment.model.Order;
import com.example.prm392_assignment.model.OrderRepository;
import java.util.List;

public class OrderManagementActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private OrderRepository orderRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);

        orderRepository = new OrderRepository(this);
        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadOrders();
    }

    private void loadOrders() {
        List<Order> orders = orderRepository.getAllOrders();
        adapter = new OrderHistoryAdapter(orders, order -> {
            boolean deleted = orderRepository.deleteOrder(order.getId());
            if (deleted) {
                Toast.makeText(this, "Order deleted", Toast.LENGTH_SHORT).show();
                orders.remove(order);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Failed to delete order", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}

