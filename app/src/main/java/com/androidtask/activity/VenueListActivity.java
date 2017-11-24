package com.androidtask.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.androidtask.R;
import com.androidtask.adapter.FragmentTabsAdapter;
import com.androidtask.adapter.VenuesListAdapter;
import com.androidtask.fragment.FavoriteVenueFragment;
import com.androidtask.fragment.AllVenuesFragment;
import com.androidtask.helper.Constants;
import com.androidtask.helper.PermissionUtils;
import com.androidtask.model.Venue;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class VenueListActivity extends AppCompatActivity implements VenuesListAdapter.OnVenueClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionUtils.PermissionResultCallback {
    private final String TAG = VenueListActivity.class.getName();

    private int type = Constants.VENUE_GENERAL;

    private AllVenuesFragment allVenuesFragment;
    private FavoriteVenueFragment favoriteVenueFragment;
    private final static int PLAY_SERVICES_REQUEST = 1000;
    private final static int REQUEST_CHECK_SETTINGS = 2000;

    private Location mLastLocation;


    private GoogleApiClient mGoogleApiClient;


    private LatLng latLng;

    ArrayList<String> permissions = new ArrayList<>();
    PermissionUtils permissionUtils;
    private ViewPager viewPager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_list);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.app_name));
        }

        setSupportActionBar(toolbar);
        getLocationPermission();
        if (checkPlayServices()) {
            buildGoogleApiClient();
        }
        getLocation();
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        initFragments(savedInstanceState);
        setupViewPager();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void setupViewPager() {
        FragmentTabsAdapter adapter = new FragmentTabsAdapter(getSupportFragmentManager());
        switch (type) {
            case Constants.VENUE_GENERAL: {
                adapter.addFragment(allVenuesFragment, getString(R.string.popular));
                adapter.addFragment(favoriteVenueFragment, getString(R.string.favorites));
                viewPager.setOffscreenPageLimit(2);
                break;
            }
        }
        viewPager.setAdapter(adapter);
    }

    private void initFragments(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            loadFromSavedInstanceState(savedInstanceState);
            return;
        }


        allVenuesFragment = new AllVenuesFragment();
        favoriteVenueFragment = new FavoriteVenueFragment();


    }

    private void loadFromSavedInstanceState(Bundle savedInstanceState) {
        allVenuesFragment = (AllVenuesFragment) getSupportFragmentManager().getFragment(savedInstanceState, AllVenuesFragment.TAG);
//        favoriteVenueFragment = (FavoriteVenueFragment) getSupportFragmentManager().getFragment(savedInstanceState, FavoriteVenueFragment.TAG);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, AllVenuesFragment.TAG, allVenuesFragment);
//        getSupportFragmentManager().putFragment(outState, FavoriteVenueFragment.TAG, favoriteVenueFragment);

    }

    public void setPosition(int position) {
        this.position = position;
    }

    private int position;

    @Override
    public void onVenuItemClick(Venue venue, int position) {
        setPosition(position);
        Intent intent = new Intent(this, VenueDetailActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_VENUE, venue);

        startActivityForResult(intent, Constants.ACTIVITY_RESULT);


    }

    private void getLocationPermission() {
        permissionUtils = new PermissionUtils(this);

        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionUtils.check_permission(permissions, getString(R.string.permission_text), 1);


    }

    private void getLocation() {

        if (isPermissionGranted) {

            try {
                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
                if (mLastLocation != null)
                    latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            } catch (SecurityException e) {
                e.printStackTrace();
            }

        }

    }


    /**
     * Creating google api client object
     */

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(VenueListActivity.this, REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });


    }


    /**
     * Method to verify google play services on the device
     */

    private boolean checkPlayServices() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode,
                        PLAY_SERVICES_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                    default:
                        break;
                }
                break;
            case Constants.ACTIVITY_RESULT:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                     if(allVenuesFragment!=null){
                         allVenuesFragment.setFavPositionAtZero(getPosition(),true);
                     }
                        break;
                    case Constants.CODE_UNFAVORITE:
                        if(allVenuesFragment!=null) {
                            allVenuesFragment.setFavPositionAtZero(getPosition(), false);
                        }
                        break;
                    default:
                        break;
                }
                break;

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }


    // Permission check functions


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    boolean isPermissionGranted;

    @Override
    public void PermissionGranted(int request_code) {
        Log.i("PERMISSION", "GRANTED");
        isPermissionGranted = true;
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY", "GRANTED");
    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION", "DENIED");
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION", "NEVER ASK AGAIN");
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public int getPosition() {
        return position;
    }
}
