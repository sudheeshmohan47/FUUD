package com.foodu.android.Adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.foodu.android.GetLocationFragment;
import com.foodu.android.Model.Items;
import com.foodu.android.R;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;


public  class FoodAdapter extends UltimateViewAdapter<FoodAdapter.SimpleAdapterViewHolder> {
    private List<Items> mValues;
    FragmentActivity activity;
    private int mDuration = 300;
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mLastPosition = 5;

    private boolean isFirstOnly = true;

    public FoodAdapter(FragmentActivity activity, List<Items> list) {
        this.mValues = list;
        this.activity = activity;
    }

    @Override
    public void onBindViewHolder(final SimpleAdapterViewHolder holder, int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= mValues.size() : position < mValues.size()) && (customHeaderView != null ? position > 0 : true)) {

            if (mValues.size() > 0) {
                Items item = mValues.get(customHeaderView != null ? position - 1 : position);
                holder.bindHolder(item, position);

            /*    if (!isFirstOnly || position > mLastPosition) {
                    for (Animator anim : getAdapterAnimations(holder.itemView, AdapterAnimationType.ScaleIn)) {
                        anim.setDuration(mDuration).start();
                        anim.setInterpolator(mInterpolator);
                    }
                    mLastPosition = position;
                } else {
                    ViewHelper.clear(holder.itemView);
                }*/
            }
            //((SimpleAdapterViewHolder) holder).textViewSample.setText(stringList.get(customHeaderView != null ? position - 1 : position));
            // ((ViewHolder) holder).itemView.setActivated(selectedItems.get(position, false));
        }

    }

    @Override
    public int getAdapterItemCount() {
        return mValues.size();
    }

    @Override
    public SimpleAdapterViewHolder getViewHolder(View view) {
        return new SimpleAdapterViewHolder(view, false);
    }

    @Override
    public SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_location, parent, false);
        SimpleAdapterViewHolder vh = new SimpleAdapterViewHolder(v, true);
        return vh;
    }

    public void remove(int position) {
        remove(mValues, position);
    }

    public void clear() {
        clear(mValues);
    }

    @Override
    public void toggleSelection(int pos) {
        super.toggleSelection(pos);
    }

    @Override
    public void setSelected(int pos) {
        super.setSelected(pos);
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void clearSelection(int pos) {
        super.clearSelection(pos);
    }


    public void swapPositions(int from, int to) {
        swapPositions(mValues, from, to);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        swapPositions(fromPosition, toPosition);
//        notifyItemMoved(fromPosition, toPosition);
        super.onItemMove(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        remove(position);
        // notifyItemRemoved(position);
//        notifyDataSetChanged();
        super.onItemDismiss(position);
    }
//
//    private int getRandomColor() {
//        SecureRandom rgen = new SecureRandom();
//        return Color.HSVToColor(150, new float[]{
//                rgen.nextInt(359), 1, 1
//        });
//    }

    public void setOnDragStartListener(OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
    public void refreshList(ArrayList<Items> items){
        this.mValues = items;
        notifyDataSetChanged();
    }
    public void addMore(ArrayList<Items> items){
        this.mValues.addAll(items);
        notifyDataSetChanged();
    }


    public class SimpleAdapterViewHolder extends UltimateRecyclerviewViewHolder implements View.OnClickListener {

        public View mView = null;
        TextView txtTitle;
        TextView txtDesc;
        ParseImageView ivLocation;
        TextView txtAddress;
        RatingBar ratingbar;
        int position;
        ProgressBar progressBarSample;
        TextView txtShopname;
        String url = "";

        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);

            if (isItem) {
                mView = itemView;
                txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
                txtShopname = (TextView) itemView.findViewById(R.id.txtShopTitle);
                txtAddress = (TextView) itemView.findViewById(R.id.txtShopAddress);
                ratingbar = (RatingBar) itemView.findViewById(R.id.ratingBar);
                //  txtDesc = (TextView)view.findViewById(R.id.txtDesc);
                ivLocation = (ParseImageView) itemView.findViewById(R.id.imgLocRow);

                itemView.setOnClickListener(this);

                progressBarSample = (ProgressBar) itemView.findViewById(R.id.progressbar);
                progressBarSample.setVisibility(View.GONE);
                mView = itemView;
            }
        }


        public void bindHolder(Items location, int pos) {

            txtTitle.setText(location.getTitle() + " " + pos);
            txtShopname.setText(location.getRestaurant().getName());
            txtAddress.setText(location.getRestaurant().getAddress());
            float rating =0;
            rating = Float.valueOf(location.getRating());
            ratingbar.setRating(rating);

            //  txtDesc.setText(location.getDescription());
            if (location.getImageArray().size() > 0) {
                ParseObject photoObj = location.getImageArray().get(0);
                ParseFile photoFile = photoObj.getParseFile("thumbnail1x");


                if (photoFile != null) {
                    url = photoFile.getUrl();
                    ivLocation.setParseFile(photoFile);
                    Drawable placeholder = ContextCompat.getDrawable(activity, R.drawable.placeholder);
                    ivLocation.setPlaceholder(placeholder);
                    ivLocation.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            // nothing to do
                        }
                    });
                }

                position = pos;

            }



        }
        @Override
        public void onClick(View v) {
            GetLocationFragment.navigate(activity, ivLocation, mValues.get(position), url);
        }
        @Override
        public void onItemSelected () {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear () {
            itemView.setBackgroundColor(0);
        }

    }
    public Items getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < mValues.size())
            return mValues.get(position);
        else return null;
    }
}