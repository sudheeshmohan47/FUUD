package com.foodu.android;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodu.android.Event.LocationEvent;
import com.foodu.android.Model.Items;
import com.foodu.android.Utilities.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;

import de.greenrobot.event.EventBus;

/**
 * Created by ANDRO01 on 05-Jan-16.
 */
public class DetailActivity extends AppCompatActivity {


    private CollapsingToolbarLayout collapsingToolbarLayout;

    @SuppressWarnings("ConstantConditions")
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.activity_detail_view);

        ViewCompat.setTransitionName(findViewById(R.id.appbar), Constants.EXTRA_IMAGE);
        supportPostponeEnterTransition();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EventBus.getDefault().register(this);
        LocationEvent event = EventBus.getDefault().getStickyEvent(LocationEvent.class);
        Items model = event.getLocationModel();

       final  String itemTitle = getIntent().getStringExtra(Constants.EXTRA_TITLE);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        System.out.println("TITLE " + itemTitle);
     //   collapsingToolbarLayout.setTitle(itemTitle);

        //setRefreshToolbarEnable(collapsingToolbarLayout,false);


        final ImageView image = (ImageView) findViewById(R.id.ivBigImage);
        Picasso.with(this).load(getIntent().getStringExtra(Constants.EXTRA_IMAGE)).into(image, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        applyPalette(palette);

                    }
                });
            }

            @Override
            public void onError() {

            }
        });

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(model.getTitle());

    }





    @Override public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.primary_dark);
        int primary = getResources().getColor(R.color.primary);
        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
      //  updateBackground((FloatingActionButton) findViewById(R.id.fab), palette);
        supportStartPostponedEnterTransition();
    }

    private void updateBackground(FloatingActionButton fab, Palette palette) {
        int lightVibrantColor = palette.getLightVibrantColor(getResources().getColor(android.R.color.white));
        int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.accent));

        //fab.setRippleColor(lightVibrantColor);
       // fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
    }
    public static void setRefreshToolbarEnable(CollapsingToolbarLayout collapsingToolbarLayout,
                                               boolean refreshToolbarEnable) {
        try {
            Field field = CollapsingToolbarLayout.class.getDeclaredField("mRefreshToolbar");
            field.setAccessible(true);
            field.setBoolean(collapsingToolbarLayout, refreshToolbarEnable);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onEvent(LocationEvent event) {
    }
}
