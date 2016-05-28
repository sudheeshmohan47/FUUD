package com.foodu.android.Model;

import com.foodu.android.Utilities.Constants;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

/**
 * Created by ANDRO01 on 14-Jan-16.
 */
@ParseClassName("restaurants")
public class Restaurants extends ParseObject {

    public String getName() {
        return getString(Constants.RESTAURENT_NAME);
    }

    public void setName(String title) {
        put(Constants.RESTAURENT_NAME, title);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint(Constants.ITEM_LOCATION);
    }

    public void setLocation(ParseGeoPoint location) {
        put(Constants.ITEM_LOCATION, location);
    }

    public String getUpadatedDate() {
        return getString(Constants.UPDATED_AT);
    }

    public void setUpadatedDate(String upadatedDate) {
        put(Constants.ITEM_DESCRIPTION, upadatedDate);
    }

    public String getCreatedDate() {
        return getString(Constants.CREATED_AT);
    }

    public void setCreatedDate(String createdDate) {
        put(Constants.CREATED_AT, createdDate);
    }
    public String getAddress() {
        return getString(Constants.ITEM_ADDRESS);
    }

    public void setAddress(String address) {
        put(Constants.ITEM_ADDRESS, address);
    }

}
