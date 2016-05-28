package com.foodu.android;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.foodu.android.Adapter.FoodAdapter;
import com.foodu.android.Event.LocationEvent;
import com.foodu.android.Model.Items;
import com.foodu.android.Model.Restaurants;
import com.foodu.android.Utilities.Constants;
import com.foodu.android.Utilities.PermissionUtil;
import com.foodu.android.Utilities.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by ANDRO01 on 22-Dec-15.
 */
public class GetLocationFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private static final int ERROR_DIALOG_REQUEST = 1;

    private static final int REQUEST_LOCATION = 0;

    /* Permission required for accessing location.*/
    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;

    int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));


    public static final String TAG = "GetLocationActivity";
    private static final int ADD_LOCATION= 0;

    private View rootView;
    private ArrayList<Items> itemsList;
    private UltimateRecyclerView rvLocations;
    private LinearLayoutManager manager;
    private FoodAdapter adapter;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private double latitude = 0.0, longitude=0.0;
    private ProgressDialog pdialog;
    private View mLayout;
    private boolean firsttime = true;
    private Handler handler = new Handler();
    private Runnable runnable;
    private boolean canloadMore = false;
    private boolean isLoadingMore = false, isSwiping = false;
    private int limit = 0;

    public GetLocationFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_getlocation, container, false);
        init();
        buildGoogleApiClient();

        if (Utils.checkForLocationService(getActivity()) ) {

            pdialog.show();
            getCurrentLocation();

        } else {
            Utils.enableLocation(getActivity());
        }

        runnable = new Runnable() {
            @Override
            public void run() {
                fetchLocationResults();
            }
        };

        return rootView;
    }


    private void init(){


        mLayout=(FrameLayout)rootView.findViewById(R.id.linParent);
        itemsList =new ArrayList<>();
        manager=new LinearLayoutManager(getActivity());
        rvLocations=(UltimateRecyclerView)rootView.findViewById(R.id.rvLocation);
        rvLocations.setLayoutManager(manager);
        pdialog = new ProgressDialog(getActivity());
        pdialog.setCancelable(false);
        pdialog.setTitle("Loading Current Location...");
        // rvLocations.setHasFixedSize(false);

        adapter = new FoodAdapter(getActivity(), itemsList);
        rvLocations.setAdapter(adapter);

        adapter.setCustomLoadMoreView(LayoutInflater.from(getActivity())
                .inflate(R.layout.custom_bottom_progressbar, null));
        rvLocations.enableLoadmore();
        rvLocations.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {

                isLoadingMore = true;
                isSwiping = false;
                System.out.println("Inside Loadmore");
                Toast.makeText(getActivity(), " Inside Loadmore ", Toast.LENGTH_SHORT).show();
                handler.removeCallbacks(runnable);
                handler.post(runnable);
                //fetchLocationResults();
            }
        });

        rvLocations.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                isSwiping = true;
                canloadMore = false;
                isLoadingMore = false;
                limit = 0;
                handler.removeCallbacks(runnable);
                handler.post(runnable);
            //    fetchLocationResults();

            }
        });

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void onEvent(LocationEvent event) {
    }

    @Override
    public void onConnected(Bundle bundle) {

        System.out.println("onConnected");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {

            //getting the latitude value
            latitude = mLastLocation.getLatitude();
            //getting the longitude value
            longitude = mLastLocation.getLongitude();
            System.out.println("onConnected Lat " + latitude + " lon " + longitude);
            if(firsttime ) {
                // fetchLocationResults();
                firsttime = false;
                handler.removeCallbacks(runnable);
                handler.post(runnable);
            }

        }else {

            if (Utils.checkForLocationService(getActivity()) ) {

                pdialog.show();
                getCurrentLocation();

            } else {
                //  Utils.enableLocation(GetLocationActivity.this);
            }
        }

    }


    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        System.out.println("onLocaChanged Lat " + latitude + " lon " + longitude);
        // fetchLocationResults();
        if(firsttime ) {
            //handler.removeCallbacks(runnable);
            handler.post(runnable);
        }else{
            if (pdialog.isShowing())
                pdialog.hide();
        }

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
    @Override
    public void onConnectionSuspended(int i) {
        Log.d("GettingLocation", "onConnectionFailed");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("GettingLocation", "onConnectionFailed");
    }

   /* public static void navigate(AppCompatActivity activity, View transitionImage, LocationModel locmodel) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(Constants.EXTRA_IMAGE, locmodel.getImageList().get(0));
        intent.putExtra(Constants.EXTRA_TITLE, locmodel.getTitle());

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, Constants.EXTRA_IMAGE);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }
*/

    public static void navigate(FragmentActivity activity, View transitionImage, Items locmodel,String url) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(Constants.EXTRA_IMAGE, url);
        intent.putExtra(Constants.EXTRA_TITLE, locmodel.getTitle());
        EventBus.getDefault().postSticky(new LocationEvent(locmodel));

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, Constants.EXTRA_IMAGE);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private void fetchLocationResults(){



        ParseObject.registerSubclass(Items.class);
        ParseObject.registerSubclass(Restaurants.class);
        ParseQuery<Items> query = new ParseQuery<Items>(Constants.TABLE_NAME_ITEMS);

        if(canloadMore ==true)
        {
            query.setSkip(limit);
            query.setLimit(15);
        }
        else
        {
            query.setLimit(15);
        }

        ParseGeoPoint geopoint = new ParseGeoPoint(latitude,longitude);
        ParseQuery<ParseObject> locQuery = new ParseQuery<ParseObject>(Constants.TABLE_RESTAURANTS);
        locQuery.whereNear(Constants.ITEM_LOCATION, geopoint);
        query.include(Constants.ITEM_IMAGEARRAY);
        query.include(Constants.RESTAURENT);
        query.whereMatchesQuery(Constants.RESTAURENT, locQuery);

        query.findInBackground(new FindCallback<Items>() {

            public void done(List<Items> markers, ParseException e) {

                if (pdialog.isShowing())
                    pdialog.hide();

                if (e == null) {

                    limit = limit + markers.size();
                    System.out.println("Markers size " + markers.size());
                    if (markers.size() > 0) {

                        canloadMore = true;
                        if (isSwiping) {
                            adapter.refreshList((ArrayList<Items>) markers);
                            manager.scrollToPosition(0);

                        } else if(isLoadingMore){
                            adapter.addMore((ArrayList<Items>)markers);

                        }else{
                            itemsList.addAll(markers);
                            adapter.notifyDataSetChanged();
                        }

                    } else {
                        canloadMore = false;
                        rvLocations.disableLoadmore();
                    }

                    isLoadingMore = false;

                } else {
                    if (pdialog.isShowing())
                        pdialog.hide();
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error could not fetch data!", Toast.LENGTH_LONG).show();

                }
            }
        });
        firsttime = false;

    }




    private boolean checkServices() {
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, getActivity(), ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(getActivity(), "Cannot connnect to mapping Service", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /*For Starting activity from Recyclerview Adapter*/
    public void startActivity(int position){

        /*System.out.println("pos " + position + " size " + itemsList .size());
        Intent intent=new Intent(GetLocationActivity.this,SelectMapActivity.class);
        Bundle bundle =new Bundle();
        bundle.putInt("position", position);
        intent.putExtra("bundle",bundle);
        startActivity(intent);*/
    }

    /*For getting current location coordinates */
    private void getCurrentLocation() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestLocationPermission();

        }else{
            currentLocation();

        }

    }

    private void currentLocation()
    {
        System.out.println("Inside currentLocation()");
        try {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        }catch(SecurityException e){
            e.printStackTrace();
        }

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity() )
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    private void requestLocationPermission() {

        /**
         * Requests the Location permission.
         * If the permission has been denied previously, a SnackBar will prompt the user to grant the
         * permission, otherwise it is requested directly.
         */

        Log.i("photam", "Location permission has NOT been granted. Requesting permission.");

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
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
                            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_LOCATION,
                                    REQUEST_LOCATION);
                        }
                    })
                    .show();
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission_group.LOCATION},
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
