package com.example.prm392_assignment.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "skincare_sales.db";
    private static final int DATABASE_VERSION = 4;

    // Table names
    public static final String TABLE_USER = "User";
    public static final String TABLE_PRODUCT = "Product";
    public static final String TABLE_CART_ITEM = "CartItem";
    public static final String TABLE_ORDER = "OrderTable";
    public static final String TABLE_ORDER_ITEM = "OrderItem";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // User table
        db.execSQL("CREATE TABLE " + TABLE_USER + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "email TEXT UNIQUE, " +
                "password TEXT, " +
                "role TEXT)");

        // Product table
        db.execSQL("CREATE TABLE " + TABLE_PRODUCT + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "price REAL NOT NULL DEFAULT 0.0, " +
                "image TEXT, " +
                "brand TEXT, " +
                "category TEXT, " +
                "skinType TEXT, " +
                "usageInstructions TEXT, " +
                "expiryDate TEXT, " + // Stored as ISO-8601 string (e.g., "2025-12-31")
                "stock INTEGER NOT NULL DEFAULT 0)");

        // CartItem table
        db.execSQL("CREATE TABLE " + TABLE_CART_ITEM + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER, " +
                "productId INTEGER, " +
                "quantity INTEGER, " +
                "FOREIGN KEY(userId) REFERENCES User(id), " +
                "FOREIGN KEY(productId) REFERENCES Product(id))");

        // Order table
        db.execSQL("CREATE TABLE " + TABLE_ORDER + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER, " +
                "date TEXT, " +
                "totalPrice REAL, " +
                "FOREIGN KEY(userId) REFERENCES User(id))");

        // OrderItem table
        db.execSQL("CREATE TABLE " + TABLE_ORDER_ITEM + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "orderId INTEGER, " +
                "productId INTEGER, " +
                "quantity INTEGER, " +
                "price REAL, " +
                "FOREIGN KEY(orderId) REFERENCES OrderTable(id), " +
                "FOREIGN KEY(productId) REFERENCES Product(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }
}

