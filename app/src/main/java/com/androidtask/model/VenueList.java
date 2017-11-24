package com.androidtask.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class VenueList {

    private int page =0;

    @Expose
    @SerializedName("venues")
    private ArrayList<Venue> venueArrayList;



    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<Venue> getVenues() {
        return venueArrayList;
    }

    public void setMovies(ArrayList<Venue> venues) {
        this.venueArrayList = venues;
    }

}
