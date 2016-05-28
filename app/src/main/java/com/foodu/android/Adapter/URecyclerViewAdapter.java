package com.foodu.android.Adapter;

import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.foodu.android.Model.LocationModel;
import com.foodu.android.R;
import com.foodu.android.Widgets.RobotoTextview;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;


public  class URecyclerViewAdapter extends UltimateViewAdapter<URecyclerViewAdapter.SimpleAdapterViewHolder> {
    private List<LocationModel> mValues;
    AppCompatActivity activity;
    private int mDuration = 300;
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mLastPosition = 5;

    private boolean isFirstOnly = true;
    public URecyclerViewAdapter(AppCompatActivity activity,List<LocationModel> list ) {
        this.mValues = list;
        this.activity = activity;
    }

    @Override
    public void onBindViewHolder(final SimpleAdapterViewHolder holder, int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= mValues.size() : position < mValues.size()) && (customHeaderView != null ? position > 0 : true)) {

            if(mValues.size() > 0) {
                LocationModel item = mValues.get(customHeaderView != null ? position - 1 : position);
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


    public class SimpleAdapterViewHolder extends UltimateRecyclerviewViewHolder implements View.OnClickListener{

        public  View mView = null;
        RobotoTextview txtTitle;
        RobotoTextview txtDesc;
        ImageView ivLocation;
        int position;
        ProgressBar progressBarSample;
        RobotoTextview txtShopname;

        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);

            if (isItem) {
                mView = itemView;
                txtTitle = (RobotoTextview)itemView.findViewById(R.id.txtTitle);
                txtShopname = (RobotoTextview)itemView.findViewById(R.id.txtShopTitle);
                //  txtDesc = (TextView)view.findViewById(R.id.txtDesc);
                ivLocation=(ImageView)itemView.findViewById(R.id.imgLocRow);

                itemView.setOnClickListener(this);

                progressBarSample = (ProgressBar) itemView.findViewById(R.id.progressbar);
                progressBarSample.setVisibility(View.GONE);
               mView = itemView;
            }
        }


        public void bindHolder(LocationModel location, int pos){

            txtTitle.setText(location.getTitle() + " "+pos);
            txtShopname.setText(location.getStoreName());
            //  txtDesc.setText(location.getDescription());
            if(location.getImageList().size() > 0){
                Uri imageUri = Uri.parse(location.getImageList().get(0));

                // Picasso.with(GetLocationActivity.this).load(imageUri.toString()).into(ivLocation);
               /* PicassoCache.getPicassoInstance(GetLocationActivity.this).load(imageUri.toString()) .transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                        .fit()
                        .centerInside()
                        .into(ivLocation);*/
                Picasso.with(activity)
                        .load(location.getImageList().get(0))
                        /*.transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                        .fit()
                        .centerCrop()*/
                        .into(ivLocation);
            }

            position=pos;

        }


        @Override
        public void onClick(View v) {
            //startActivity(position);
           // GetLocationActivity.navigate(activity, ivLocation, mValues.get(position));
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

    }

    public LocationModel getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < mValues.size())
            return mValues.get(position);
        else return null;
    }

}