package com.foodu.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ANDRO01 on 18-Jan-16.
 */
public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment ;
        fragment =new AddItemFragment();
        manager.beginTransaction()
                .add(R.id.item_Parent, fragment)
                .commit();
    }
}
