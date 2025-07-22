package com.example.prm392_assignment.model;
import java.io.Serializable;
import java.util.Date;

public class Product implements Serializable {

    private int id;
    private String name;
    private String description;
    private double price;
    private String image; // Store image resource name or URI
    private String brand;
    private String category;           // e.g., Cleanser, Moisturizer, Serum
    private String skinType;           // e.g., Oily, Dry, Combination, Sensitive
    private String usageInstructions;
    private Date expiryDate;
    private int stock;

    public Product() {}

    public Product(int id, String name, String description, double price, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }
    public Product(int id, String name, String description, double price, String image,
                           String brand, String category, String skinType, String usageInstructions, Date expiryDate, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.brand = brand;
        this.category = category;
        this.skinType = skinType;
        this.usageInstructions = usageInstructions;
        this.expiryDate = expiryDate;
        this.stock = stock;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSkinType() { return skinType; }
    public void setSkinType(String skinType) { this.skinType = skinType; }

    public String getUsageInstructions() { return usageInstructions; }
    public void setUsageInstructions(String usageInstructions) { this.usageInstructions = usageInstructions; }
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

}

