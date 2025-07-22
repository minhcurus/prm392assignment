package com.example.prm392_assignment.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ProductRepository {
    private DatabaseHelper dbHelper;
    public ProductRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCT,
                null, // get all columns
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                product.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                product.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
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
            product.setBrand(cursor.getString(cursor.getColumnIndexOrThrow("brand")));
            product.setCategory(cursor.getString(cursor.getColumnIndexOrThrow("category")));
            product.setSkinType(cursor.getString(cursor.getColumnIndexOrThrow("skinType")));
            product.setUsageInstructions(cursor.getString(cursor.getColumnIndexOrThrow("usageInstructions")));
            String expiryStr = cursor.getString(cursor.getColumnIndexOrThrow("expiryDate"));
            if (expiryStr != null && !expiryStr.isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date expiryDate = sdf.parse(expiryStr);
                    product.setExpiryDate(expiryDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                    product.setExpiryDate(null);
                }
            } else {
                product.setExpiryDate(null); // Handle case where expiryDate is missing
            }
            product.setStock(cursor.getInt(cursor.getColumnIndexOrThrow("stock")));
            cursor.close();
            return product;
        }
        return null;
    }

    public void insertSampleProducts() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_PRODUCT, null);
        if (cursor.moveToFirst() && cursor.getInt(0) == 0) {
            // Insert sample data with random expiry dates
            insertProduct(db, "Gentle Cleanser", "Gentle foaming cleanser for sensitive skin", 12.99, randomExpiryDate(), "Cetaphil", "Cleanser", "Sensitive", 150, "https://www.cetaphil.com/on/demandware.static/-/Library-Sites-RefArchSharedLibrary/default/dw2e3cbf8c/052384-GSC-16oz_Front.PNG");
            insertProduct(db, "Daily Moisturizer", "Hydrating daily moisturizer with hyaluronic acid", 18.99, randomExpiryDate(), "CeraVe", "Moisturizer", "Dry", 120, "https://imageskincare.vn/wp-content/uploads/2024/08/daily-prevention-ultra-defense-moisturizer-spf-50-5.png");
            insertProduct(db, "Sunscreen SPF 50", "SPF 50+ broad spectrum protection", 15.99, randomExpiryDate(), "La Roche-Posay", "Sunscreen", "All", 200,"https://zomedical.vn/wp-content/uploads/2019/04/Kem-chong-nang-cho-da-nhay-cam-BROAD-SPECTRUM-SUNSCREEN-SPF-50.jpg");

            insertProduct(db, "Vitamin C Serum", "Brightening vitamin C serum with antioxidants", 24.99, randomExpiryDate(), "The Ordinary", "Serum", "All", 100,"https://www.larocheposay.vn/-/media/project/loreal/brand-sites/lrp/apac/vn/products/redermic/c10/la-roche-posay-productpage-anti-aging-pure-vitamin-c10-30ml-3337875660570-front.png?cx=0&amp;ch=600&amp;cy=0&amp;cw=600&hash=23241E2FA3F33F60DEC4E4FC44D1B93C");
            insertProduct(db, "Retinol Night Cream", "Anti-aging night cream with retinol", 29.99, randomExpiryDate(), "Neutrogena", "Night Cream", "Mature", 90,"https://skininspired.in/cdn/shop/files/Retinol_Night_Cream_by_SkinInspired.jpg?v=1746084826");
            insertProduct(db, "Hyaluronic Acid Serum", "Intense hydration booster serum", 21.99, randomExpiryDate(), "Vichy", "Serum", "Dry", 75,"https://product.hstatic.net/1000006063/product/2-5_f5e5a59ae618429cb880edc05862cb3e_1024x1024.png");
            insertProduct(db, "Niacinamide Serum", "Pore minimizing serum with 10% niacinamide", 19.99, randomExpiryDate(), "The Ordinary", "Serum", "Oily", 85,"https://www.newu.in/cdn/shop/files/1_a218dd3e-7f1c-4961-b4e0-44400f53e449_1024x1024.jpg?v=1706523989");
            insertProduct(db, "AHA/BHA Exfoliant", "Chemical exfoliant for smooth skin", 22.99, randomExpiryDate(), "Paula's Choice", "Exfoliant", "Combination", 60,"https://media.paulaschoice-eu.com/image/upload/f_auto,q_auto,dpr_auto/content/paulachoice/ExpertAdvice/Exfoliants/aha-vs-bha/BHA-AHA-ProductBHA.jpg?_i=AG");

            insertProduct(db, "Eye Cream", "Anti-aging eye cream with peptides", 32.99, randomExpiryDate(), "Olay", "Eye Care", "Mature", 40,"https://file.hstatic.net/1000006063/file/download__20__1efebef29f97439ca23651e23147ea32.jpeg");
            insertProduct(db, "Face Mask", "Hydrating sheet mask pack (5 pieces)", 14.99, randomExpiryDate(), "Innisfree", "Mask", "All", 180,"https://bioaqua.com.pk/cdn/shop/files/SADOER_Aloe_Vera_Soft_Moisturizing_Face_Sheet_Mask.webp?v=1724713549");
            insertProduct(db, "Clay Mask", "Purifying clay mask for oily skin", 16.99, randomExpiryDate(), "Aztec Secret", "Mask", "Oily", 70,"https://m.media-amazon.com/images/I/71niyVLPlUL._UF350,350_QL80_.jpg");
            insertProduct(db, "Lip Balm", "Moisturizing lip balm with SPF 15", 4.99, randomExpiryDate(), "Burt's Bees", "Lip Care", "All", 300,"https://img.nivea.com/-/media/miscellaneous/media-center-items/temp/6e6e4c9c3b29414baf84b1a1246a2831-web_1010x1180_transparent_png.png");

            insertProduct(db, "Foundation", "Long-lasting liquid foundation", 28.99, randomExpiryDate(), "Maybelline", "Makeup", "All", 130,"https://product.hstatic.net/200000863577/product/dation_-_2018-30ml-worldwide-packshot-110-honey-80039698-190962_master_3ce985839544489e8252256567d69684_master.jpg");
            insertProduct(db, "Concealer", "Full coverage concealer", 16.99, randomExpiryDate(), "NARS", "Makeup", "All", 100,"https://happyskincosmetics.com/cdn/shop/files/CREAMBEIGECONCEALER3_2048x.jpg?v=1715507558");
            insertProduct(db, "Mascara", "Volumizing waterproof mascara", 13.99, randomExpiryDate(), "L'Oréal", "Makeup", "All", 160,"");
            insertProduct(db, "Lipstick", "Matte finish lipstick", 11.99, randomExpiryDate(), "MAC", "Makeup", "All", 140,"");
            insertProduct(db, "Blush", "Natural glow powder blush", 15.99, randomExpiryDate(), "Milani", "Makeup", "All", 110,"");
            insertProduct(db, "Eyeshadow Palette", "12-color neutral eyeshadow palette", 25.99, randomExpiryDate(), "Urban Decay", "Makeup", "All", 50,"");

            insertProduct(db, "Makeup Brushes Set", "Professional makeup brush set (10 pieces)", 35.99, randomExpiryDate(), "Real Techniques", "Tool", "All", 100,"");
            insertProduct(db, "Beauty Sponge", "Latex-free makeup blending sponge", 8.99, randomExpiryDate(), "Beautyblender", "Tool", "All", 220,"");
            insertProduct(db, "Facial Roller", "Rose quartz facial massage roller", 19.99, randomExpiryDate(), "Herbivore", "Tool", "All", 75,"");
            insertProduct(db, "Cleansing Brush", "Electric facial cleansing brush", 45.99, randomExpiryDate(), "Foreo", "Tool", "All", 60,"");

            insertProduct(db, "Body Lotion", "Nourishing body lotion with shea butter", 13.99, randomExpiryDate(), "Aveeno", "Body Care", "Dry", 90,"");
            insertProduct(db, "Body Wash", "Moisturizing body wash with natural oils", 9.99, randomExpiryDate(), "Dove", "Body Care", "Dry", 110,"");
            insertProduct(db, "Hand Cream", "Intensive repair hand cream", 7.99, randomExpiryDate(), "Neutrogena", "Body Care", "Dry", 100,"");
            insertProduct(db, "Body Scrub", "Exfoliating sugar body scrub", 17.99, randomExpiryDate(), "Tree Hut", "Body Care", "All", 80,"");

            insertProduct(db, "Shampoo", "Sulfate-free hydrating shampoo", 14.99, randomExpiryDate(), "OGX", "Hair Care", "Dry", 120,"https://m.media-amazon.com/images/I/81uj0KS8nBL._SL1500_.jpg");
            insertProduct(db, "Conditioner", "Deep conditioning hair treatment", 16.99, randomExpiryDate(), "Pantene", "Hair Care", "Damaged", 100,"https://images.ctfassets.net/ya8mvjlg9l8b/5zK4cACMC2Bm72z4PjRfVL/865560ac11b2f072023dcb761da98021/coconut-milk-conditioner-13oz-FOP-cloudy-gradient.webp");
            insertProduct(db, "Hair Serum", "Frizz control and shine serum", 18.99, randomExpiryDate(), "L'Oréal", "Hair Care", "Frizzy", 70,"https://media6.ppl-media.com//tr:h-750,w-750,c-at_max,dpr-2,q-40/static/img/product/393712/streax-hair-serum-vitalised-with-walnut-oil-45-ml-86-19-12_8_display_1738057057_2aec30a5.jpg");
            insertProduct(db, "Hair Mask", "Weekly intensive repair hair mask", 21.99, randomExpiryDate(), "Moroccanoil", "Hair Care", "Damaged", 60,"");

            insertProduct(db, "Luxury Face Oil", "Premium anti-aging face oil blend", 49.99, randomExpiryDate(), "Sunday Riley", "Serum", "Mature", 30,"https://glycelene.com/cdn/shop/files/websitepicskinsupp_800x.png?v=1693492700");
            insertProduct(db, "Gold Serum", "24k gold infused luxury serum", 89.99, randomExpiryDate(), "Tatcha", "Serum", "All", 20,"https://swissbeauty.in/cdn/shop/files/SB-SC52_FOP_1080x.jpg?v=1748634115");
        }
        cursor.close();
        db.close();
    }

    // Generates a random expiry date between 1 month and 3 years from now in YYYY-MM-DD format
    private String randomExpiryDate() {
        Calendar calendar = Calendar.getInstance();
        int daysToAdd = (30) + new Random().nextInt((3 * 365) - 30); // From 1 month to 3 years
        calendar.add(Calendar.DAY_OF_YEAR, daysToAdd);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    private void insertProduct(SQLiteDatabase db, String name, String description, double price,
                               String expiryDate, String brand, String category,
                               String skinType, int stock, String imageURL) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("price", price);
        values.put("expiryDate", expiryDate);
        values.put("brand", brand);
        values.put("category", category);
        values.put("skinType", skinType);
        values.put("stock", stock);
        values.put("image", imageURL);

        db.insert("Product", null, values);
    }

    public long addProduct(Product product) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", product.getName());
        values.put("description", product.getDescription());
        values.put("price", product.getPrice());
        values.put("image", product.getImage());
        values.put("brand", product.getBrand());
        values.put("category", product.getCategory());
        values.put("skinType", product.getSkinType());
        values.put("usageInstructions", product.getUsageInstructions());
        values.put("expiryDate", product.getExpiryDate().toString()); // Save as String
        values.put("stock", product.getStock());

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
        values.put("brand", product.getBrand());
        values.put("category", product.getCategory());
        values.put("skinType", product.getSkinType());
        values.put("usageInstructions", product.getUsageInstructions());
        values.put("expiryDate", product.getExpiryDate().toString());
        values.put("stock", product.getStock());

        db.update("Product", values, "id = ?", new String[]{String.valueOf(product.getId())});
    }

    public void deleteProduct(int productId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Product", "id = ?", new String[]{String.valueOf(productId)});
        db.close();
    }
}