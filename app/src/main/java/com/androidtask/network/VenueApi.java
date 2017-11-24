package com.androidtask.network;



import com.androidtask.model.VenueList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;



public class VenueApi {
    public interface DSGAPIVenue {
        @GET("api/venue/")
        Call<VenueList> getAllVenues();
    }


}
