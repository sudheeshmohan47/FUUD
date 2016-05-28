package com.foodu.android.Model;

import java.util.ArrayList;

/**
 * Created by ANDRO01 on 22-Dec-15.
 */
public class LocationModel {


    private String title;
    private String description;
    private String storeName;
    private double latitude;
    private double longitude;
    ArrayList<String> imageList;
    private String upadatedDate;
    private String createdDate;
    private int rating;
    private int price;

    public  LocationModel(){

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public ArrayList<String> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }
    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUpadatedDate() {
        return upadatedDate;
    }

    public void setUpadatedDate(String upadatedDate) {
        this.upadatedDate = upadatedDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
