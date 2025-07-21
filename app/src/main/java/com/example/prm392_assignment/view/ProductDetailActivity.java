package com.example.prm392_assignment.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_assignment.R;
import com.example.prm392_assignment.model.Product;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView tvName, tvDescription, tvPrice;
    private ImageView ivProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        tvName = findViewById(R.id.tvName);
        tvDescription = findViewById(R.id.tvDescription);
        tvPrice = findViewById(R.id.tvPrice);
        ivProduct = findViewById(R.id.ivProduct);

        Product product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            tvName.setText(product.getName());
            tvDescription.setText(product.getDescription());
            tvPrice.setText(String.format("$%.2f", product.getPrice()));

            int imageResId = getResources().getIdentifier(product.getImage(), "drawable", getPackageName());
            if (imageResId != 0) {
                ivProduct.setImageResource(imageResId);
            } else {
                ivProduct.setImageResource(R.drawable.ic_image_placeholder);
                Toast.makeText(this, "Không tìm thấy ảnh, dùng ảnh mặc định", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Không có thông tin sản phẩm", Toast.LENGTH_LONG).show();
        }
    }
}
