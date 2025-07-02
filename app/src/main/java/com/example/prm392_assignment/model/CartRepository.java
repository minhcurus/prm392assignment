package com.example.prm392_assignment.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CartRepository {
    private DatabaseHelper dbHelper;

    public CartRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void addToCart(int userId, int productId, int quantity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Check if item already in cart
        Cursor cursor = db.query(DatabaseHelper.TABLE_CART_ITEM,
                new String[]{"id", "quantity"},
                "userId=? AND productId=?",
                new String[]{String.valueOf(userId), String.valueOf(productId)},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            // Update quantity
            int id = cursor.getInt(0);
            int oldQty = cursor.getInt(1);
            ContentValues values = new ContentValues();
            values.put("quantity", oldQty + quantity);
            db.update(DatabaseHelper.TABLE_CART_ITEM, values, "id=?", new String[]{String.valueOf(id)});
            cursor.close();
        } else {
            // Insert new
            ContentValues values = new ContentValues();
            values.put("userId", userId);
            values.put("productId", productId);
            values.put("quantity", quantity);
            db.insert(DatabaseHelper.TABLE_CART_ITEM, null, values);
        }
        db.close();
    }

    public List<CartItem> getCartItems(int userId) {
        List<CartItem> items = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CART_ITEM,
                new String[]{"id", "userId", "productId", "quantity"},
                "userId=?",
                new String[]{String.valueOf(userId)},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                CartItem item = new CartItem(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getInt(3)
                );
                items.add(item);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return items;
    }

    public void updateCartItem(int id, int quantity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", quantity);
        db.update(DatabaseHelper.TABLE_CART_ITEM, values, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void removeCartItem(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_CART_ITEM, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void clearCart(int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_CART_ITEM, "userId=?", new String[]{String.valueOf(userId)});
        db.close();
    }
}

