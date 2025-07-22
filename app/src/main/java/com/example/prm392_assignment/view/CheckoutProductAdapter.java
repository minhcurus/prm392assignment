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
import com.example.prm392_assignment.model.CartItem;
import com.example.prm392_assignment.model.Product;

import java.util.List;

public class CheckoutProductAdapter extends RecyclerView.Adapter<CheckoutProductAdapter.ViewHolder> {
    private List<CartItem> cartItems;
    private List<Product> allProducts;

    public CheckoutProductAdapter(List<CartItem> cartItems, List<Product> allProducts) {
        this.cartItems = cartItems;
        this.allProducts = allProducts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checkout_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        for (Product product : allProducts) {
            if (product.getId() == item.getProductId()) {
                holder.tvName.setText(product.getName());
                holder.tvPrice.setText(String.format("$%.2f", product.getPrice()));
                holder.tvQuantity.setText("x" + item.getQuantity());
                if (product.getImage() != null && !product.getImage().isEmpty()) {
                    holder.ivImage.setImageURI(Uri.parse(product.getImage()));
                } else {
                    holder.ivImage.setImageResource(R.drawable.ic_image_placeholder);
                }

                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCheckoutProductName);
            tvPrice = itemView.findViewById(R.id.tvCheckoutProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvCheckoutProductQuantity);
            ivImage = itemView.findViewById(R.id.ivCheckoutProductImage);
        }
    }
}

