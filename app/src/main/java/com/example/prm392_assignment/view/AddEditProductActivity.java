package com.example.prm392_assignment.view;

import static android.app.Activity.RESULT_OK;
import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_assignment.R;
import com.example.prm392_assignment.model.Product;
import com.example.prm392_assignment.model.ProductRepository;

public class AddEditProductActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1001;
    private ImageView imageView;
    private Uri imageUri;
    private int productId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);
        imageView = findViewById(R.id.productImageView);
        productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        if (productId != -1) {
            ProductRepository repo = new ProductRepository(this);
            Product product = repo.getProductById(productId); // Implement this method if missing
            if (product != null) {
                ((EditText) findViewById(R.id.etProductName)).setText(product.getName());
                ((EditText) findViewById(R.id.etProductDescription)).setText(product.getDescription());
                ((EditText) findViewById(R.id.etProductPrice)).setText(String.valueOf(product.getPrice()));

                if (product.getImage() != null) {
                    imageUri = Uri.parse(product.getImage());
                    imageView.setImageURI(imageUri);
                }
            }
        }
        imageView.setOnClickListener(v -> chooseImageFromGallery());

        findViewById(R.id.btnSaveProduct).setOnClickListener(v -> saveProduct());
    }

    private void chooseImageFromGallery() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        intent.setType("image/*");
        intent.putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, 1); // Only pick one image
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            imageView.setImageURI(imageUri);
        }
    }

    private void saveProduct() {
        String name = ((EditText) findViewById(R.id.etProductName)).getText().toString();
        String description = ((EditText) findViewById(R.id.etProductDescription)).getText().toString();
        double price = Double.parseDouble(((EditText) findViewById(R.id.etProductPrice)).getText().toString());

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setImage(imageUri != null ? imageUri.toString() : null);

        ProductRepository repo = new ProductRepository(this);
        if (productId != -1) {
            product.setId(productId);
            repo.updateProduct(product);
            Toast.makeText(this, "Product updated!", Toast.LENGTH_SHORT).show();
        } else {
            repo.addProduct(product);
            Toast.makeText(this, "Product saved!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}

