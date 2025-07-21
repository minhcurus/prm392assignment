package com.example.prm392_assignment.view;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_assignment.R;
import com.example.prm392_assignment.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    public interface OnAddToCartClickListener {
        void onAddToCart(Product product);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position, Product product);
    }

    private List<Product> productList;
    private OnAddToCartClickListener listener;
    private OnItemLongClickListener longClickListener;
    private boolean isAdmin;

    public ProductAdapter(List<Product> productList, OnAddToCartClickListener listener, boolean isAdmin) {
        this.productList = new ArrayList<>(productList); // Create a copy to avoid modifying the original list
        this.listener = listener;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvName.setText(product.getName());
        holder.tvDescription.setText(product.getDescription());
        holder.tvPrice.setText("$" + String.format("%.2f", product.getPrice()));
        if (product.getImage() != null) {
            holder.ivProductImage.setImageURI(Uri.parse(product.getImage()));
        } else {
            holder.ivProductImage.setImageResource(R.drawable.ic_image_placeholder);
        }
        if (isAdmin) {
            holder.btnAddToCart.setVisibility(View.GONE);
        } else {
            holder.btnAddToCart.setVisibility(View.VISIBLE);
            holder.btnAddToCart.setOnClickListener(v -> listener.onAddToCart(product));
        }
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(holder.getAdapterPosition(), product);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Method to update the product list based on search results
    public void updateList(List<Product> newList) {
        productList.clear();
        productList.addAll(newList);
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription, tvPrice;
        ImageView ivProductImage;
        Button btnAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvDescription = itemView.findViewById(R.id.tvProductDescription);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}