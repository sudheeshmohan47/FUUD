package com.foodu.android.login.BaseFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;

/**
 * Created by Sudheesh on 16-May-16.
 */
public class BaseLoginFragment extends Fragment {
    public int device_width;
    public int device_height;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDisplayWidth();

    }

    private void getDisplayWidth(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        device_width = (int)(displayMetrics.widthPixels / displayMetrics.density);
        device_height = (int)(displayMetrics.heightPixels / displayMetrics.density);

    }
}
