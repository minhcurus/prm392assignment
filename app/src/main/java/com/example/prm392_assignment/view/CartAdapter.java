package com.example.prm392_assignment.view;

import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_assignment.R;
import com.example.prm392_assignment.model.CartItem;
import com.example.prm392_assignment.model.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    public interface CartActionListener {
        void onQuantityChanged(CartItem item, int newQty);
        void onRemove(CartItem item);
    }

    private List<CartItem> cartItems;
    private List<Product> allProducts;
    private CartActionListener listener;

    public CartAdapter(List<CartItem> cartItems, List<Product> allProducts, CartActionListener listener) {
        this.cartItems = cartItems;
        this.allProducts = allProducts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        Product product = getProductById(item.getProductId());
        if (product != null) {
            holder.tvName.setText(product.getName());
            holder.tvPrice.setText("$" + String.format("%.2f", product.getPrice()));
            if (product.getImage() != null && !product.getImage().isEmpty()) {
                holder.ivCartProductImage.setImageURI(Uri.parse(product.getImage()));
            } else {
                holder.ivCartProductImage.setImageResource(R.drawable.ic_image_placeholder);
            }
        }
        holder.etQuantity.setText(String.valueOf(item.getQuantity()));
        holder.btnRemove.setOnClickListener(v -> listener.onRemove(item));
        holder.etQuantity.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String qtyStr = holder.etQuantity.getText().toString();
                if (!TextUtils.isEmpty(qtyStr)) {
                    int newQty = Integer.parseInt(qtyStr);
                    if (newQty > 0 && newQty != item.getQuantity()) {
                        listener.onQuantityChanged(item, newQty);
                        item.setQuantity(newQty);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    private Product getProductById(int productId) {
        for (Product p : allProducts) {
            if (p.getId() == productId) return p;
        }
        return null;
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        EditText etQuantity;
        Button btnRemove;
        ImageView ivCartProductImage;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCartProductName);
            tvPrice = itemView.findViewById(R.id.tvCartProductPrice);
            etQuantity = itemView.findViewById(R.id.etCartQuantity);
            btnRemove = itemView.findViewById(R.id.btnRemoveCartItem);
            ivCartProductImage = itemView.findViewById(R.id.ivCartProductImage);
        }
    }
}

