package com.example.prm392_assignment.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private DatabaseHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long registerUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("role", user.getRole());
        long id = db.insert(DatabaseHelper.TABLE_USER, null, values);
        db.close();
        return id;
    }

    public User loginUser(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USER,
                new String[]{"id", "name", "email", "password", "role"},
                "email=? AND password=?",
                new String[]{email, password},
                null, null, null);
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
            cursor.close();
        }
        db.close();
        return user;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USER,
                new String[]{"id"},
                "email=?",
                new String[]{email},
                null, null, null);
        boolean exists = (cursor != null && cursor.moveToFirst());
        if (cursor != null) cursor.close();
        db.close();
        return exists;
    }

    public int updateUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        int rows = db.update(DatabaseHelper.TABLE_USER, values, "id=?", new String[]{String.valueOf(user.getId())});
        db.close();
        return rows;
    }

    public User getUserById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USER,
                new String[]{"id", "name", "email", "password", "role"},
                "id=?",
                new String[]{String.valueOf(id)},
                null, null, null);
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
            cursor.close();
        }
        db.close();
        return user;
    }
    public void insertAdminIfNotExists() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_USER,
                null,
                "email = ?",
                new String[]{"admin@gmail.com"},
                null,
                null,
                null
        );

        if (!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put("name", "Admin");
            values.put("email", "admin@gmail.com");
            values.put("password", "admin123");
            values.put("role", "Admin");
            db.insert(DatabaseHelper.TABLE_USER, null, values);
            Log.d("ADMIN_INIT", "Admin user inserted.");
        } else {
            Log.d("ADMIN_INIT", "Admin already exists.");
        }

        cursor.close();
        db.close();
    }
    public void updateUserAdmin(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        db.update("User", values, "id = ?", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public void deleteUser(int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("User", "id = ?", new String[]{String.valueOf(userId)});
        db.close();
    }
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USER,
                new String[]{"id", "name", "email", "password", "role"},
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                userList.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return userList;
    }


}

