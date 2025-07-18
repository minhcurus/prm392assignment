package com.example.prm392_assignment.view;

import android.os.Bundle;
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
        adapter = new OrderHistoryAdapter(orders);
        recyclerView.setAdapter(adapter);
    }
}

