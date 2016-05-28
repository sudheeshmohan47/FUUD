package com.foodu.android.Event;

import com.foodu.android.Model.Items;

import java.util.ArrayList;

/**
 * Created by ANDRO01 on 30-Dec-15.
 */
public class LocationEvent {

    private ArrayList<Items> locList;
    private Items locationModel;

    public LocationEvent(ArrayList<Items> list){
        this.locList=list;
    }
    public LocationEvent(Items model){
        this.locationModel=model;
    }

    public ArrayList<Items>  getLocationList(){

        return locList;
    }
    public Items  getLocationModel(){

        return locationModel;
    }
}
