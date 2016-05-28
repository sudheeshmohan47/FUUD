package com.foodu.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foodu.android.RatingBar.ProperRatingBar;

/**
 * Created by ANDRO01 on 22-Jan-16.
 */
public class AddItemFragment extends Fragment {
    View rootView;

    ProperRatingBar ratingbar;
    public AddItemFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_add_item, container, false);
       // ratingbar = (ProperRatingBar)rootView.findViewById(R.id.ratingBar);
       // ratingbar.setRating(2);
        return  rootView;
    }
}
