package com.foodu.android.login.BaseActivities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

/**
 * Created by Sudheesh on 15-May-16.
 */
public class BaseLoginActivity extends AppCompatActivity {
    public int device_width;
    public int device_height;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDisplayWidth();



    }

    private void getDisplayWidth(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        device_width = (int)(displayMetrics.widthPixels / displayMetrics.density);
        device_height = (int)(displayMetrics.heightPixels / displayMetrics.density);

    }
}


