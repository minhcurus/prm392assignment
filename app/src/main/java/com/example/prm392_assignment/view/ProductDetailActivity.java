package com.example.prm392_assignment.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_assignment.model.Product;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView tvName, tvDescription, tvPrice;
    private ImageView ivProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // Load layout không dùng R
            int layoutId = getResources().getIdentifier("activity_product_detail", "layout", getPackageName());
            setContentView(layoutId);

            // Tìm view không dùng R.id
            tvName = findViewById(getResources().getIdentifier("tvName", "id", getPackageName()));
            tvDescription = findViewById(getResources().getIdentifier("tvDescription", "id", getPackageName()));
            tvPrice = findViewById(getResources().getIdentifier("tvPrice", "id", getPackageName()));
            ivProduct = findViewById(getResources().getIdentifier("ivProduct", "id", getPackageName()));

            // Nhận sản phẩm từ Intent
            Product product = (Product) getIntent().getSerializableExtra("product");

            if (product != null) {
                tvName.setText(product.getName());
                tvDescription.setText(product.getDescription());
                tvPrice.setText(String.format("$%.2f", product.getPrice()));

                // Load ảnh drawable không dùng R
                int imageId = getResources().getIdentifier(product.getImage(), "drawable", getPackageName());
                if (imageId != 0) {
                    ivProduct.setImageResource(imageId);
                } else {
                    Toast.makeText(this, "Không tìm thấy ảnh", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Có lỗi xảy ra khi load chi tiết sản phẩm", Toast.LENGTH_LONG).show();
        }
    }
}
