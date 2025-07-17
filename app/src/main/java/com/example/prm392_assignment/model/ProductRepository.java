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

    public void insertSampleProducts() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Only insert if table is empty
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_PRODUCT, null);
        if (cursor.moveToFirst() && cursor.getInt(0) == 0) {
            insertProduct(db, "Cleanser", "Gentle foaming cleanser", 12.99, "");
            insertProduct(db, "Moisturizer", "Hydrating daily moisturizer", 18.99, "");
            insertProduct(db, "Sunscreen", "SPF 50+ broad spectrum", 15.99, "");
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
    public void deleteProduct(int productId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Product", "id = ?", new String[]{String.valueOf(productId)});
        db.close();
    }
}

