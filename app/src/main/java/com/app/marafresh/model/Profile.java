package com.app.marafresh.model;

import java.io.Serializable;

public class Profile implements Serializable {



    private String FullName;
    private String PhoneNumber;

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getFirekey() {
        return Firekey;
    }

    public void setFirekey(String firekey) {
        Firekey = firekey;
    }

    private String Image;
    private String uid;
    private String key;
    private String email;
    private String Timestamp;
    private String Firekey;

    public Profile(String email,
                  String FullName, String PhoneNumber,
                   String uid,
                   String key, String Image, String Timestamp, String Firekey){



        this.FullName=FullName;
        this.PhoneNumber=PhoneNumber;
        this.uid=uid;
        this.key=key;
        this.Image=Image;
        this.Timestamp=Timestamp;
        this.Image=Image;
        this.Firekey=Firekey;
        this.email=email;


    }



    public Profile profile;


    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }


    public Profile(String uid, Profile profile) {
        this.uid = uid;
        this.profile = profile;
    }

    public Profile() {
    }
}