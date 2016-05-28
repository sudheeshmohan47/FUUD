package com.foodu.android.Model;

import com.foodu.android.Utilities.Constants;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by ANDRO01 on 14-Jan-16.
 */
@ParseClassName("items")
public class Items extends ParseObject {

    public Items(){

    }

    public String getTitle() {
        return getString(Constants.ITEM_TITLE);
    }

    public void setTitle(String title) {
        put(Constants.ITEM_TITLE, title);
    }
    public String getItemAddress() {
        return getString(Constants.ITEM_ADDRESS);
    }

    public void setItemAddress(String itemAddress) {
        put(Constants.ITEM_ADDRESS, itemAddress);
    }


    public String getDescription() {
        return getString(Constants.ITEM_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(Constants.ITEM_DESCRIPTION, description);
    }

    public String getCreatedDate() {
        return getString(Constants.CREATED_AT);
    }

    public void setCreatedDate(String createdDate) {
        put(Constants.CREATED_AT, createdDate);
    }

    public int getRating() {
        return getInt(Constants.ITEM_RATING);
    }

    public void setRating(int rating) {
        put(Constants.ITEM_RATING, rating);
    }

    public String getUpadatedDate() {
        return getString(Constants.UPDATED_AT);
    }

    public void setUpadatedDate(String upadatedDate) {
        put(Constants.UPDATED_AT, upadatedDate);
    }

    public int getPrice() {
        return getInt(Constants.ITEM_PRICE);
    }

    public void setPrice(int price) {
        put(Constants.ITEM_PRICE, price);
    }

    public Restaurants getRestaurant() {
        return (Restaurants)getParseObject(Constants.RESTAURENT);
    }

    public void setRestaurant(Restaurants restaurants) {
        put(Constants.RESTAURENT, restaurants);
    }
    public List<ParseObject> getImageArray() {
        return getList(Constants.ITEM_IMAGEARRAY);
    }

    public void setImageArray(List<ParseObject> array) {
        put(Constants.ITEM_IMAGEARRAY, array);
    }

}
