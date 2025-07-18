package com.example.prm392_assignment.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderRepository {
    private DatabaseHelper dbHelper;

    public OrderRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long placeOrder(int userId, double totalPrice, List<CartItem> cartItems) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        long orderId = -1;
        try {
            ContentValues orderValues = new ContentValues();
            orderValues.put("userId", userId);
            orderValues.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            orderValues.put("totalPrice", totalPrice);
            orderId = db.insert(DatabaseHelper.TABLE_ORDER, null, orderValues);
            for (CartItem item : cartItems) {
                ContentValues itemValues = new ContentValues();
                itemValues.put("orderId", orderId);
                itemValues.put("productId", item.getProductId());
                itemValues.put("quantity", item.getQuantity());
                // Get product price
                double price = getProductPrice(db, item.getProductId());
                itemValues.put("price", price);
                db.insert(DatabaseHelper.TABLE_ORDER_ITEM, null, itemValues);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return orderId;
    }

    private double getProductPrice(SQLiteDatabase db, int productId) {
        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCT, new String[]{"price"}, "id=?", new String[]{String.valueOf(productId)}, null, null, null);
        double price = 0;
        if (cursor != null && cursor.moveToFirst()) {
            price = cursor.getDouble(0);
            cursor.close();
        }
        return price;
    }

    public List<Order> getOrdersByUser(int userId) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_ORDER,
                new String[]{"id", "userId", "date", "totalPrice"},
                "userId=?",
                new String[]{String.valueOf(userId)},
                null, null, "date DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getDouble(3)
                );
                orders.add(order);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return orders;
    }

    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_ORDER,
                new String[]{"id", "userId", "date", "totalPrice"},
                null, null, null, null, "date DESC");

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getInt(0));
                order.setUserId(cursor.getInt(1));
                order.setDate(cursor.getString(2));
                order.setTotalPrice(cursor.getDouble(3));
                orderList.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orderList;
    }

    public List<OrderItem> getOrderItems(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_ORDER_ITEM,
                new String[]{"id", "orderId", "productId", "quantity", "price"},
                "orderId=?",
                new String[]{String.valueOf(orderId)},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                OrderItem item = new OrderItem(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getDouble(4)
                );
                items.add(item);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return items;
    }
    public boolean deleteOrder(int orderId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(DatabaseHelper.TABLE_ORDER_ITEM, "orderId = ?", new String[]{String.valueOf(orderId)});
            int rowsAffected = db.delete(DatabaseHelper.TABLE_ORDER, "id = ?", new String[]{String.valueOf(orderId)});

            db.setTransactionSuccessful();
            return rowsAffected > 0;
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}

