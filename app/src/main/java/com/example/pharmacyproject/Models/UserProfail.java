package com.example.pharmacyproject.Models;

public class UserProfail {
    private String uid ;
    private String name ;
    private String email ;
    private String mobileNumber ;
    private String image ;
    //ملاحظة الباسسورد ما بتسجل

    public String getImage() {
        return image;
    }

    public UserProfail() {
    }

    public void setImage(String image) {
        this.image = image;
    }

    public UserProfail(String uid, String name, String email, String mobileNumber, String image) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.image = image;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
