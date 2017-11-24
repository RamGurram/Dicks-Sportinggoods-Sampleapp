package com.androidtask.helper;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.androidtask.R;
import com.google.android.gms.maps.model.LatLng;


public class StateHandler {
    public static String handleVenueDetailState(Context context, int flag) {
        String tagLine;
        switch (flag) {
            case Constants.NETWORK_ERROR: {
                tagLine = context.getString(R.string.network_error);
                break;
            }

            case Constants.SERVER_ERROR: {
                tagLine = context.getString(R.string.server_error);
                break;
            }

            default: {
                tagLine = "";
            }
        }
        return tagLine;
    }
    private float distanceBetween(LatLng latLng1, LatLng latLng2) {

        Location loc1 = new Location(LocationManager.GPS_PROVIDER);
        Location loc2 = new Location(LocationManager.GPS_PROVIDER);

        loc1.setLatitude(latLng1.latitude);
        loc1.setLongitude(latLng1.longitude);

        loc2.setLatitude(latLng2.latitude);
        loc2.setLongitude(latLng2.longitude);


        return loc1.distanceTo(loc2);
    }
}
