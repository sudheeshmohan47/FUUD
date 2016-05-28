package com.foodu.android;

import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.foodu.android.Utilities.Utils;


/**
 * Created by ANDRO01 on 26-Oct-15.
 */
public class MapActivity extends AppCompatActivity {

    Button btnOpenMaps;
    Button btnShareMaps;
    double latitude,longitude;
    LocationManager lm;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    TextView txtLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        btnOpenMaps=(Button)findViewById(R.id.btnLoadLocation);
        btnShareMaps=(Button)findViewById(R.id.btnShareLoc);
        txtLoc=(TextView)findViewById(R.id.txtLoc);

        /*Share*/
        btnShareMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String uri = "geo:" + latitude + ","
                            + longitude + "?q=" + latitude
                            + "," + longitude;
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(uri)));

            }
        });

        btnOpenMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.checkForLocationService(MapActivity.this) == true) {
                    Intent intent = new Intent(MapActivity.this, SelectMapActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    Utils.enableLocation(MapActivity.this);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(resultCode == 1){
                latitude=data.getDoubleExtra("latitude",0);
                longitude=data.getDoubleExtra("longitude",0);
                txtLoc.setText("Current Location: latitude "+latitude+", Longitude "+longitude);
            }
        }
    }



}
