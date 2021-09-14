package com.example.pharmacyproject.Models;

public class Medicine {
    private String MedicineId ;
    private String uid ;
    private String name ;
    private String type ;
    private double theCost;
    private double price;
    private String description;
    private String image;
    private boolean isAdd;

    public Medicine(String medicineId, String uid, String name, String type, double theCost, double price, String description, String image, boolean isAdd) {
        MedicineId = medicineId;
        this.uid = uid;
        this.name = name;
        this.type = type;
        this.theCost = theCost;
        this.price = price;
        this.description = description;
        this.image = image;
        this.isAdd = isAdd;
    }

    public Medicine() {
    }

    public String getMedicineId() {
        return MedicineId;
    }

    public void setMedicineId(String medicineId) {
        MedicineId = medicineId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getTheCost() {
        return theCost;
    }

    public void setTheCost(double theCost) {
        this.theCost = theCost;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }
}
