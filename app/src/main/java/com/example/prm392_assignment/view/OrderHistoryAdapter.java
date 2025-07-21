package com.example.prm392_assignment.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_assignment.R;
import com.example.prm392_assignment.model.Order;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    public interface OrderActionListener {
        void onDelete(Order order);
    }

    private List<Order> orderList;
    private OrderActionListener listener;

    // Constructor cho người dùng thường (không xóa)
    public OrderHistoryAdapter(List<Order> orderList) {
        this.orderList = orderList;
        this.listener = null; // ⚠️ BẮT BUỘC gán null để tránh crash
    }

    // Constructor cho admin (có thể xóa)
    public OrderHistoryAdapter(List<Order> orderList, OrderActionListener listener) {
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvOrderDate.setText(order.getDate());
        holder.tvOrderTotal.setText("Total: $" + String.format("%.2f", order.getTotalPrice()));
        holder.tvOrderDetails.setText("Order ID: " + order.getId());

        // Mở chi tiết đơn hàng
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), OrderDetailActivity.class);
            intent.putExtra("order_id", order.getId());
            v.getContext().startActivity(intent);
        });

        // Chỉ gán long-click khi listener khác null
        if (listener != null) {
            holder.itemView.setOnLongClickListener(v -> {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete Order")
                        .setMessage("Are you sure you want to delete this order?")
                        .setPositiveButton("Delete", (dialog, which) -> listener.onDelete(order))
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
            });
        } else {
            holder.itemView.setOnLongClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderDate, tvOrderTotal, tvOrderDetails;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvOrderDetails = itemView.findViewById(R.id.tvOrderDetails);
        }
    }
}
