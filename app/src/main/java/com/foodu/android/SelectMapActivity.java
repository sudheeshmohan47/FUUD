package com.foodu.android;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foodu.android.Model.MapModel;
import com.foodu.android.Utilities.PermissionUtil;
import com.foodu.android.Utilities.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ANDRO01 on 26-Oct-15.
 */

public class SelectMapActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener,OnMapReadyCallback {

    private static final int ERROR_DIALOG_REQUEST = 1;

    /**
     * Id to identify a camera permission request.
     */

    private static final int REQUEST_LOCATION = 0;
    /**
     * Permission required for accessing location.
     */
    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    public static final String TAG = "SelectMapActivity";
    private static final int ADD_LOCATION= 0;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Button btnSelectMapLoc;
    private double latitude = 0.0, longitude=0.0;
    private ProgressDialog pdialog;
    private SupportMapFragment mapFragment;
    private ArrayList<Placeinfo> infoList;
    private Placeinfo info;
    private int mapType;
    private CircleImageView ivType;
    private Map<Marker, MapModel> markerMap = new HashMap<>();
    private CameraPosition cp;
    private View mLayout;
    private Marker currentMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_map);
        mLayout=(View)findViewById(R.id.relParent);
        infoList = new ArrayList<>();

        btnSelectMapLoc = (Button) findViewById(R.id.btnSelectMapLoc);
        pdialog = new ProgressDialog(SelectMapActivity.this);
        ivType = (CircleImageView) findViewById(R.id.imgType);
        pdialog.setCancelable(false);
        pdialog.setTitle("Loading Current Location...");

        setupMapIfNeeded();

        if (Utils.checkForLocationService(SelectMapActivity.this) == true) {

            pdialog.show();
            getCurrentLocation();

        } else {
            Utils.enableLocation(SelectMapActivity.this);
        }


        // Setting a custom info window adapter for the google map
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker marker) {

                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.info_view, null);

                // Getting reference to the TextView to set latitude
                TextView title = (TextView) v.findViewById(R.id.txtInfoView);

                // Getting reference to the TextView to set longitude
                ImageView imgInfoView = (ImageView) v.findViewById(R.id.imgInfoView);


                title.setText(marker.getTitle());
                if (markerMap.get(marker) != null)
                    imgInfoView.setImageResource(markerMap.get(marker).getImageUrl());


                // Returning the view containing InfoWindow contents
                return v;

            }
        });
        ivType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapType == GoogleMap.MAP_TYPE_TERRAIN) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    mapType = GoogleMap.MAP_TYPE_SATELLITE;
                    ivType.setImageResource(R.drawable.terrain);

                } else if (mapType == GoogleMap.MAP_TYPE_SATELLITE) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    mapType = GoogleMap.MAP_TYPE_TERRAIN;
                    ivType.setImageResource(R.drawable.satellite);

                }
            }
        });
        btnSelectMapLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                Log.e("Location", "before selecting Latitude: " + latitude + " longitude" + longitude);
                setResult(1, intent);
                finish();

            }
        });


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                final double lat, lng;
                lat = latLng.latitude;
                lng = latLng.longitude;

                AlertDialog.Builder builder = new AlertDialog.Builder(SelectMapActivity.this);
                builder.setTitle("Add Location")
                        .setMessage("Do you want to add the location?")
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addLocationDetails(lat, lng);
                            }
                        });

                AppCompatDialog alert = builder.create();
                alert.show();


            }
        });


    }

    private void addLocationDetails(double latitude,double longitude){

        Intent intent =new Intent(SelectMapActivity.this, AddLocationActivity.class);
        Bundle bundle=new Bundle();
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("longitude", longitude);
        intent.putExtra("location", bundle);
        startActivityForResult(intent, ADD_LOCATION);

    }

    private void setupMapIfNeeded() {
        if (mMap == null) {
            mapType = GoogleMap.MAP_TYPE_TERRAIN;
            buildGoogleApiClient();
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap = mapFragment.getMap();
            mMap.setMyLocationEnabled(true);
            mapFragment.getMapAsync(this);
        }
    }

    public void onPause() {
        // mapFragment.onPause();
        super.onPause();

        cp = mMap.getCameraPosition();
        System.out.println("onPause" + " cp.target.latitude " + cp.target.latitude + " cp.target.longitude " + cp.target.longitude + " cp.zoom " + cp.zoom);
        // mMap = null;
    }

    @Override
    protected void onResume() {
        //  mapFragment.onResume();
        super.onResume();

        setupMapIfNeeded();

        if(Utils.checkForLocationService(SelectMapActivity.this) == true){

            //mapFragment.getMap().clear();
        //   pdialog.show();
            MapsInitializer.initialize(SelectMapActivity.this);

        }else{
            Utils.enableLocation(SelectMapActivity.this);
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void gotoLocation(double lat, double lng, float zoom) {
        Log.d("value", "gotoLocation called");
        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.moveCamera(update);
    }

    private boolean checkServices() {
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(SelectMapActivity.this, "Cannot connnect to mapping Service", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean initMap() {
        if (mMap == null) {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap = mapFragment.getMap();
            mMap.setMyLocationEnabled(true);
        }
        return (mMap != null);
    }



    @Override
    public void onConnected(Bundle bundle) {

        System.out.println("onConnected");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {

            if(pdialog.isShowing()){
                pdialog.hide();
            }
            //getting the latitude value
            double latitudeValue = mLastLocation.getLatitude();
            Log.d("value", "" + latitudeValue);
            //getting the longitude value
            double longitudeValue = mLastLocation.getLongitude();
            if (checkServices()) {
                if (initMap()) {

                    gotoLocation(latitudeValue, longitudeValue, 200);

                    // Other supported types include: MAP_TYPE_NORMAL,
                    // MAP_TYPE_TERRAIN, MAP_TYPE_HYBRID and MAP_TYPE_NONE
                    ivType.setImageResource(R.drawable.satellite);
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    //mMap.setMyLocationEnabled(true);

                    //Setting up the marker
                    MarkerOptions marker = new MarkerOptions()
                            .title("Current Location!")
                            .position(new LatLng(latitudeValue, longitudeValue))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    mMap.clear();
                    currentMarker =  mMap.addMarker(marker);
                    list();
                    getNearbyLocations();

                }
            }
        }
        if (cp != null) {

            LatLng latLng = new LatLng(cp.target.latitude, cp.target.longitude);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, cp.zoom);
            mMap.moveCamera(update);

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("GettingLocation", "onConnectionFailed");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("GettingLocation", "onConnectionFailed");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /*For getting current location coordinates */
    private void getCurrentLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestLocationPermission();

        }else{
            currentLocation();

        }

    }

    @Override
    public void onLocationChanged(Location location) {


        if (pdialog.isShowing())
            pdialog.hide();

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        System.out.println("onLocaChanged Lat "+latitude+ " lon "+longitude);
        if(currentMarker != null){
            currentMarker.remove();
        }

        currentMarker = mMap.addMarker(new MarkerOptions()
                .title("Current Location!")
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        // Showing the current location in Google Map
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));

        // Zoom in the Google Map
      //  mMap.animateCamera(CameraUpdateFactory.zoomTo(15));



        // TextView locationTv = (TextView) findViewById(R.id.latlongLocation);

       /* LatLng latLng = new LatLng(latitude, longitude);
        marker.setPosition(latLng);*/
        //    mMap.addMarker(new MarkerOptions().position(latLng));
        //  mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        // locationTv.setText("Latitude:" + latitude + ", Longitude:" + longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void setMarkers(ArrayList<Placeinfo> markers) {
        for (int i = 0; i < markers.size(); i++) {
            Placeinfo info = markers.get(i);

            Marker m = mMap.addMarker(new MarkerOptions()

                    .position(new LatLng(info.getLatitude(), info.getLongitude()))

                    .title(info.getTitle()));
            MapModel model = new MapModel();
            model.setImageUrl(R.drawable.forest);

            markerMap.put(m, model);

        }


    }
    private void currentLocation()
    {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }catch (SecurityException e){
            e.printStackTrace();
        }

    }

    public void list() {

        info = new Placeinfo(10.030502, 76.304776, "Al Shifa");
        infoList.add(info);

        info = new Placeinfo(10.030217, 76.304883, "CAfe Arabia");
        infoList.add(info);

        info = new Placeinfo(10.031072, 76.304229, "Random place");
        infoList.add(info);

        info = new Placeinfo(10.030428, 76.304131, "G tower");
        infoList.add(info);

        info = new Placeinfo(10.031594, 76.303400, "Vodafone store");
        infoList.add(info);

        info = new Placeinfo(10.032327, 76.303133, "Workammo");
        infoList.add(info);

        info = new Placeinfo(10.033084, 76.302771, "Dolphin");
        infoList.add(info);

        info = new Placeinfo(10.033434, 76.302479, "TVS");
        infoList.add(info);

        setMarkers(infoList);
    }

    private void getNearbyLocations(){

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

         mMap=googleMap;
         list();


    }

    private class Placeinfo {

        private double latitude;
        private double longitude;
        String title;

        public Placeinfo(double lat, double lng, String title) {
            this.latitude = lat;
            this.longitude = lng;
            this.title = title;

        }

        public double getLongitude() {
            return longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public String getTitle() {
            return title;
        }


    }

    private void requestLocationPermission() {

        /**
         * Requests the Location permission.
         * If the permission has been denied previously, a SnackBar will prompt the user to grant the
         * permission, otherwise it is requested directly.
         */

            Log.i("photam", "Location permission has NOT been granted. Requesting permission.");

            // BEGIN_INCLUDE(camera_permission_request)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Provide an additional rationale to the user if the permission was not granted
                // and the user would benefit from additional context for the use of the permission.
                // For example if the user has previously denied the permission.
                Log.i("Photam",
                        "Displaying location permission rationale to provide additional context.");
                Snackbar.make(mLayout, "Location Permission is needed to show the location",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(SelectMapActivity.this, PERMISSIONS_LOCATION,
                                        REQUEST_LOCATION);
                            }
                        })
                        .show();
            } else {

                // Camera permission has not been granted yet. Request it directly.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission_group.LOCATION},
                        REQUEST_LOCATION);
            }
            // END_INCLUDE(camera_permission_request)
        }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_LOCATION) {
            Log.i(TAG, "Received response for location permissions request.");

            // We have requested multiple permissions for contacts, so all of them need to be
            // checked.
            if (PermissionUtil.verifyPermissions(grantResults)) {
                // All required permissions have been granted, display contacts fragment.
                Snackbar.make(mLayout, "Locations Permissions have been granted.",
                        Snackbar.LENGTH_SHORT)
                        .show();
                currentLocation();
            } else {
                Log.i(TAG, "Location permissions were NOT granted.");
               /* Snackbar.make(mLayout, "Location permissions were NOT granted.",
                        Snackbar.LENGTH_SHORT)
                        .show();*/
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
