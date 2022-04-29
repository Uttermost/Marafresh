package com.app.marafresh.model;

import java.io.Serializable;

public class Cart implements Serializable{
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }


    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getFirekey() {
        return Firekey;
    }

    public void setFirekey(String firekey) {
        Firekey = firekey;
    }

    private String Title;
    private String Description;
    private String Quantity;




    private String Category;
    private String Photo;
    private String Price;

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public String getBusinessEmail() {
        return BusinessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        BusinessEmail = businessEmail;
    }

    private String BusinessName;
    private String BusinessEmail;


    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPaybill() {
        return Paybill;
    }

    public void setPaybill(String paybill) {
        Paybill = paybill;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    private String Location,Paybill,Telephone;
    private String CreatedAt;

    public String getSubCategory() {
        return SubCategory;
    }

    public void setSubCategory(String subCategory) {
        SubCategory = subCategory;
    }

    private String SubCategory;
    private String Firekey;


    public Cart(String Title, String Description, String Price,
                String Quantity, String Category,String SubCategory,
                String Photo, String BusinessName, String BusinessEmail, String Location,
                String Paybill, String Telephone, String CreatedAt, String Firekey) {
        this.Title = Title;
        this.Description = Description;

        this.Quantity = Quantity;
        this.Category = Category;

        this.Category = Category;
        this.SubCategory=SubCategory;

        this.Price=Price;
        this.BusinessName=BusinessName;
        this.BusinessEmail=BusinessEmail;

        this.Location=Location;
        this.Paybill=Paybill;
        this.Telephone=Telephone;


        this.Photo = Photo;

        this.CreatedAt=CreatedAt;
        this.Firekey=Firekey;
    }




}
