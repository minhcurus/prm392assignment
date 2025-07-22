package com.example.prm392_assignment.view;

import static android.app.Activity.RESULT_OK;
import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_assignment.R;
import com.example.prm392_assignment.model.Product;
import com.example.prm392_assignment.model.ProductRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditProductActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1001;
    private ImageView imageView;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private EditText brandEditText;
    private EditText categoryEditText;
    private EditText skinTypeEditText;
    private EditText usageEditText;
    private EditText stockEditText;
    private TextView expiryTextView;
    private Calendar selectedExpiryDate;
    private Uri imageUri;
    private int productId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        imageView = findViewById(R.id.ivProductImage);
        nameEditText = findViewById(R.id.editTextProductName);
        descriptionEditText = findViewById(R.id.editTextDescription);
        priceEditText = findViewById(R.id.editTextPrice);
        brandEditText = findViewById(R.id.etProductBrand);
        categoryEditText = findViewById(R.id.etProductCategory);
        skinTypeEditText = findViewById(R.id.etProductSkinType);
        usageEditText = findViewById(R.id.etProductUsage);
        stockEditText = findViewById(R.id.etProductStock);
        expiryTextView = findViewById(R.id.etProductExpiry);
        selectedExpiryDate = Calendar.getInstance();
        productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        if (productId != -1) {
            ProductRepository repo = new ProductRepository(this);
            Product product = null;
            try {
                product = repo.getProductById(productId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (product != null) {
                nameEditText.setText(product.getName());
                descriptionEditText.setText(product.getDescription());
                priceEditText.setText(String.valueOf(product.getPrice()));
                brandEditText.setText(product.getBrand());
                categoryEditText.setText(product.getCategory());
                skinTypeEditText.setText(product.getSkinType());
                usageEditText.setText(product.getUsageInstructions());
                stockEditText.setText(String.valueOf(product.getStock()));

                if (product.getExpiryDate() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    expiryTextView.setText(sdf.format(product.getExpiryDate()));
                }

                if (product.getImage() != null) {
                    imageUri = Uri.parse(product.getImage());
                    imageView.setImageURI(imageUri);
                }
            }
        }

        imageView.setOnClickListener(v -> chooseImageFromGallery());
        expiryTextView.setOnClickListener(v -> pickExpiryDate());

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

    private void pickExpiryDate() {
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedExpiryDate.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            expiryTextView.setText(sdf.format(selectedExpiryDate.getTime()));
        },
                selectedExpiryDate.get(Calendar.YEAR),
                selectedExpiryDate.get(Calendar.MONTH),
                selectedExpiryDate.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

    private void saveProduct() {
        String name = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        double price = Double.parseDouble(priceEditText.getText().toString());
        String brand = brandEditText.getText().toString();
        String category = categoryEditText.getText().toString();
        String skinType = skinTypeEditText.getText().toString();
        String usage = usageEditText.getText().toString();
        int stock = Integer.parseInt(stockEditText.getText().toString());
        Date expiry = selectedExpiryDate.getTime();

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setImage(imageUri != null ? imageUri.toString() : null);
        product.setBrand(brand);
        product.setCategory(category);
        product.setSkinType(skinType);
        product.setUsageInstructions(usage);
        product.setStock(stock);
        product.setExpiryDate(expiry);

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

