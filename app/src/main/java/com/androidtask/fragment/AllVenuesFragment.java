package com.androidtask.fragment;



import android.util.Log;

import com.androidtask.helper.Constants;
import com.androidtask.model.VenueList;
import com.androidtask.network.DSGLabsRestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllVenuesFragment extends VenueListFragment {
    public static final String TAG = AllVenuesFragment.class.getName();
    public AllVenuesFragment() {}

    @Override
    protected void loadVenues() {
        super.loadVenues();
        Call<VenueList> call = DSGLabsRestClient.getInstance()
                .getDSGVenusImpl()
                .getAllVenues();
        Callback<VenueList> callback = new Callback<VenueList>() {
            @Override
            public void onResponse(Call<VenueList> call, Response<VenueList> response) {
                if (!response.isSuccessful()) {
                    retrievalError(Constants.SERVER_ERROR);
                    return;
                }
                setTotalPages(1);
                addVenues(response.body().getVenues());
                Log.d(TAG,"Response"+response.body().getVenues().size());

            }

            @Override
            public void onFailure(Call<VenueList> call, Throwable t) {
                Log.d(TAG,"ERROR"+t.getMessage());
                retrievalError(Constants.NETWORK_ERROR);
            }
        };
        call.enqueue(callback);
    }
}
