package com.example.prm392_assignment.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private DatabaseHelper dbHelper;

    public ProductRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCT,
                new String[]{"id", "name", "description", "price", "image"},
                null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getString(4)
                );
                products.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return products;
    }

    public Product getProductById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCT, null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Product product = new Product();
            product.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            product.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
            product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
            product.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
            cursor.close();
            return product;
        }
        return null;
    }

    public void insertSampleProducts() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Only insert if table is empty
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_PRODUCT, null);
        if (cursor.moveToFirst() && cursor.getInt(0) == 0) {
            // Basic skincare essentials
            insertProduct(db, "Gentle Cleanser", "Gentle foaming cleanser for sensitive skin", 12.99, "");
            insertProduct(db, "Daily Moisturizer", "Hydrating daily moisturizer with hyaluronic acid", 18.99, "");
            insertProduct(db, "Sunscreen SPF 50", "SPF 50+ broad spectrum protection", 15.99, "");

            // Advanced skincare
            insertProduct(db, "Vitamin C Serum", "Brightening vitamin C serum with antioxidants", 24.99, "");
            insertProduct(db, "Retinol Night Cream", "Anti-aging night cream with retinol", 29.99, "");
            insertProduct(db, "Hyaluronic Acid Serum", "Intense hydration booster serum", 21.99, "");
            insertProduct(db, "Niacinamide Serum", "Pore minimizing serum with 10% niacinamide", 19.99, "");
            insertProduct(db, "AHA/BHA Exfoliant", "Chemical exfoliant for smooth skin", 22.99, "");

            // Specialized treatments
            insertProduct(db, "Eye Cream", "Anti-aging eye cream with peptides", 32.99, "");
            insertProduct(db, "Face Mask", "Hydrating sheet mask pack (5 pieces)", 14.99, "");
            insertProduct(db, "Clay Mask", "Purifying clay mask for oily skin", 16.99, "");
            insertProduct(db, "Lip Balm", "Moisturizing lip balm with SPF 15", 4.99, "");

            // Makeup essentials
            insertProduct(db, "Foundation", "Long-lasting liquid foundation", 28.99, "");
            insertProduct(db, "Concealer", "Full coverage concealer", 16.99, "");
            insertProduct(db, "Mascara", "Volumizing waterproof mascara", 13.99, "");
            insertProduct(db, "Lipstick", "Matte finish lipstick", 11.99, "");
            insertProduct(db, "Blush", "Natural glow powder blush", 15.99, "");
            insertProduct(db, "Eyeshadow Palette", "12-color neutral eyeshadow palette", 25.99, "");

            // Tools and accessories
            insertProduct(db, "Makeup Brushes Set", "Professional makeup brush set (10 pieces)", 35.99, "");
            insertProduct(db, "Beauty Sponge", "Latex-free makeup blending sponge", 8.99, "");
            insertProduct(db, "Facial Roller", "Rose quartz facial massage roller", 19.99, "");
            insertProduct(db, "Cleansing Brush", "Electric facial cleansing brush", 45.99, "");

            // Body care
            insertProduct(db, "Body Lotion", "Nourishing body lotion with shea butter", 13.99, "");
            insertProduct(db, "Body Wash", "Moisturizing body wash with natural oils", 9.99, "");
            insertProduct(db, "Hand Cream", "Intensive repair hand cream", 7.99, "");
            insertProduct(db, "Body Scrub", "Exfoliating sugar body scrub", 17.99, "");

            // Hair care
            insertProduct(db, "Shampoo", "Sulfate-free hydrating shampoo", 14.99, "");
            insertProduct(db, "Conditioner", "Deep conditioning hair treatment", 16.99, "");
            insertProduct(db, "Hair Serum", "Frizz control and shine serum", 18.99, "");
            insertProduct(db, "Hair Mask", "Weekly intensive repair hair mask", 21.99, "");

            // Premium products
            insertProduct(db, "Luxury Face Oil", "Premium anti-aging face oil blend", 49.99, "");
            insertProduct(db, "Gold Serum", "24k gold infused luxury serum", 89.99, "");
        }
        cursor.close();
        db.close();
    }

    public void insertProduct(SQLiteDatabase db, String name, String desc, double price, String image) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", desc);
        values.put("price", price);
        values.put("image", image);
        db.insert(DatabaseHelper.TABLE_PRODUCT, null, values);
    }

    public long addProduct(Product product) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", product.getName());
        values.put("description", product.getDescription());
        values.put("price", product.getPrice());
        values.put("image", product.getImage());

        long id = db.insert("Product", null, values);
        db.close();
        return id;
    }

    public void updateProduct(Product product) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", product.getName());
        values.put("description", product.getDescription());
        values.put("price", product.getPrice());
        values.put("image", product.getImage());
        db.update("Product", values, "id = ?", new String[]{String.valueOf(product.getId())});
    }

    public void deleteProduct(int productId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Product", "id = ?", new String[]{String.valueOf(productId)});
        db.close();
    }
}