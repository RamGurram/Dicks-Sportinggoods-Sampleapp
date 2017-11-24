package com.androidtask.fragment;

import android.content.Context;
import android.content.SharedPreferences;

import com.androidtask.helper.Constants;
import com.androidtask.model.Venue;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.androidtask.helper.Constants.BUNDLE_VENUE_FAV_ITEM;
import static com.androidtask.helper.Constants.PREFS_NAME;


public class FavoriteVenueFragment extends VenueListFragment {
    public static final String TAG = FavoriteVenueFragment.class.getName();

    public FavoriteVenueFragment() {}

    @Override
    protected void loadVenues() {
        SharedPreferences mPrefs = getActivity().getApplicationContext().getSharedPreferences(PREFS_NAME, 0);

        if (!mPrefs.getBoolean(Constants.BUNDLE_VENUE_FAV, false)) {
            retrievalError(Constants.LOADING_FAVORITES);
            addFavoriteVenue(null);
        } else {
            ArrayList<Venue> list = new ArrayList<>();
            list.add(0, getFavoritedVenue(getActivity()));
            addFavoriteVenue(list);
        }
    }
    public  static Venue getFavoritedVenue(Context context) {
        Venue obj = null;
        SharedPreferences mPrefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        if (mPrefs.getBoolean(Constants.BUNDLE_VENUE_FAV, false)) {
            Gson gson = new Gson();
            String json = mPrefs.getString(BUNDLE_VENUE_FAV_ITEM, "");
             obj = gson.fromJson(json, Venue.class);
        }
        return obj;

    }


}
