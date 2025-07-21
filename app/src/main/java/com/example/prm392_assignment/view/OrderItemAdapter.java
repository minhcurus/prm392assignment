package com.example.prm392_assignment.view;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_assignment.R;
import com.example.prm392_assignment.model.OrderItemWithProduct;
import com.example.prm392_assignment.model.Product;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {
    private List<OrderItemWithProduct> itemList;

    public OrderItemAdapter(List<OrderItemWithProduct> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_detail_product, parent, false); // tái dùng layout sản phẩm
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItemWithProduct item = itemList.get(position);
        Product product = item.getProduct();

        holder.tvName.setText(product.getName());
        holder.tvDescription.setText(product.getDescription());
        holder.tvPrice.setText(String.format("$%.2f", item.getPrice()));
        holder.tvQuantity.setText("Qty: " + item.getQuantity());

        if (product.getImage() != null && !product.getImage().isEmpty()) {
            holder.ivImage.setImageURI(Uri.parse(product.getImage()));
        } else {
            holder.ivImage.setImageResource(R.drawable.ic_image_placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvDescription, tvPrice, tvQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivProductImage);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvDescription = itemView.findViewById(R.id.tvProductDescription);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = new TextView(itemView.getContext());
            ((ViewGroup) itemView).addView(tvQuantity); // hoặc đặt sẵn trong XML nếu cần
        }
    }
}

