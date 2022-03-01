package com.app.marafresh.model;

import java.util.List;


public class Orders {

    private String deliveryLat;
    private String deliveryLng;
    private String FirstName;
    private String landmark;
    private String location;
    private String MobileNumber;
    private String paybill;
    private String shopMail;
    private String shopName;


    private String Status;

    public String getTransporterId() {
        return TransporterId;
    }

    public void setTransporterId(String transporterId) {
        TransporterId = transporterId;
    }

    public String getTransportername() {
        return Transportername;
    }

    public void setTransportername(String transportername) {
        Transportername = transportername;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getVehicleregnumber() {
        return vehicleregnumber;
    }

    public void setVehicleregnumber(String vehicleregnumber) {
        this.vehicleregnumber = vehicleregnumber;
    }

    private String TransporterId;
    private String Transportername;
    private String Type;
    private List<Item> items = null;
    private String key;
    private Double latitude;
    private Double longitude;
    private String note;
    private Long timestamp;
    private String total;
    private String vehicleregnumber;

    public Orders() {
    }

    public Orders(String deliveryLat, String deliveryLng, String FirstName, String landmark, String location, String MobileNumber, String paybill, String shopMail, String shopName, String Status,
                  String TransporterId,
                  String Transportername,
                  String Type, List<Item> items, String key, Double latitude, Double longitude, String note, Long timestamp, String total, String vehicleregnumber) {
        this.deliveryLat = deliveryLat;
        this.deliveryLng = deliveryLng;
        this.FirstName = FirstName;
        this.landmark = landmark;
        this.location = location;
        this.MobileNumber = MobileNumber;
        this.paybill = paybill;
        this.shopMail = shopMail;
        this.shopName = shopName;
        this.Status = Status;
        this.items = items;
        this.key = key;
        this.latitude = latitude;
        this.longitude = longitude;
        this.note = note;
        this.timestamp = timestamp;
        this.TransporterId = TransporterId;
        this.Transportername = Transportername;
        this.Type = Type;
        this.vehicleregnumber = vehicleregnumber;

        this.total = total;
    }

    public String getDeliveryLat() {
        return deliveryLat;
    }

    public void setDeliveryLat(String deliveryLat) {
        this.deliveryLat = deliveryLat;
    }

    public String getDeliveryLng() {
        return deliveryLng;
    }

    public void setDeliveryLng(String deliveryLng) {
        this.deliveryLng = deliveryLng;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.MobileNumber = mobileNumber;
    }

    public String getPaybill() {
        return paybill;
    }

    public void setPaybill(String paybill) {
        this.paybill = paybill;
    }

    public String getShopMail() {
        return shopMail;
    }

    public void setShopMail(String shopMail) {
        this.shopMail = shopMail;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }



}