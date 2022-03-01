package com.app.marafresh.model;

import java.io.Serializable;

public class CartItems implements Serializable{
    private String Title;
     private String Photo;



    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    private String Price;
    private String Quantity;

    public String getShopname() {
        return Shopname;
    }

    public void setShopname(String shopname) {
        Shopname = shopname;
    }

    public String getShopemail() {
        return Shopemail;
    }

    public void setShopemail(String shopemail) {
        Shopemail = shopemail;
    }

    private String Shopname;
    private String Shopemail;
    private String Firekey;
    private Float TotalPerITEM;
    private String key_main;
    private String  Location,Paybill,Telephone;

    public CartItems(String key_main) {
        this.key_main = key_main;
    }

    public String getKey_main() {
        return key_main;
    }

    public void setKey_main(String key_main) {
        this.key_main = key_main;
    }


    public CartItems(String title, String photo, String price, String quantity,
                     String Shopname,
                     String Shopemail, String Location,
                     String Paybill, String Telephone, String firekey, Float totalPerITEM) {
        Title = title;
        Photo = photo;
        Price = price;
        Quantity = quantity;
        Shopemail = Shopemail;
        Shopname = Shopname;

        this.Location=Location;
        this.Paybill=Paybill;
        this.Telephone=Telephone;
        Quantity = quantity;
        Firekey = firekey;
        TotalPerITEM = totalPerITEM;

    }




    public Float getTotalPerITEM() {
        return TotalPerITEM;
    }

    public void setTotalPerITEM(Float totalPerITEM) {
        TotalPerITEM = totalPerITEM;
    }

    public String getFirekey() {
        return Firekey;
    }

    public void setFirekey(String firekey) {
        Firekey = firekey;
    }



    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }




    public CartItems(String Title, String Price, String Photo, String Quantity,
                     String Shopname,
                     String Shopemail, String Location,
                     String Paybill, String Telephone, String Firekey) {
        this.Title = Title;

        this.Price=Price;

        this.Photo = Photo;
        this.Quantity=Quantity;
        this.Shopname=Shopname;
        this.Shopemail=Shopemail;

        this.Location=Location;
        this.Paybill=Paybill;
        this.Telephone=Telephone;

        this.Quantity=Quantity;
        this.Firekey=Firekey;

    }

    public CartItems() {
    }

    public CartItems(String title, String quantity) {
        Title = title;
        Quantity = quantity;
    }
}
