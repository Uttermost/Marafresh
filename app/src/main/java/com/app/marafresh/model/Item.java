package com.app.marafresh.model;



public class Item {

    private String quantity;
    private String title;

    public Item() {
    }

    public Item(String quantity, String title) {
        this.quantity = quantity;
        this.title = title;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}