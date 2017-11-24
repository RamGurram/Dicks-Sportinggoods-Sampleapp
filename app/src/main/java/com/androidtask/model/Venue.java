package com.androidtask.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class Venue implements Parcelable {

    private  boolean isFavorite=false;

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    private  float distance;

    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("verified")
    private boolean verified;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("ratingColor")
    private String ratingColor;

    @Expose
    @SerializedName("url")
    private String url;

    public String getDescription() {
        return description;
    }

    @Expose
    @SerializedName("description")
    private String description;
    @Expose
    @SerializedName("ratingSignals")
    private int ratingSignals;

    @Expose
    @SerializedName("storeId")
    private String storeId;


    @Expose
    @SerializedName("location")
    private VenueLocation location;

    @Expose
    @SerializedName("contacts")
    private ArrayList<VenueContacts> contacts  = new ArrayList<>();;

    public VenueLocation getLocation() {
        return location;
    }

    public void setLocation(VenueLocation location) {
        this.location = location;
    }

    public ArrayList<VenueContacts> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<VenueContacts> contacts) {
        this.contacts = contacts;
    }

    @Expose
    @SerializedName("photos")
    private ArrayList<VenueImage> photos  = new ArrayList<>();
    ;

    @Expose
    @SerializedName("rating")
    private double rating;




    public Venue() {
    }



    private Venue(Parcel source) {
        this.id = source.readString();
        this.verified = source.readByte() == 1;
        this.name = source.readString();
        this.ratingColor = source.readString();
        this.url = source.readString();
        source.readTypedList(this.photos, VenueImage.CREATOR);
        if (photos == null) {
            photos = new ArrayList<>();
        }
        this.ratingSignals = source.readInt();
        this.storeId = source.readString();
        this.rating = source.readDouble();
        source.readTypedList(this.contacts, VenueContacts.CREATOR);
        if (contacts == null) {
            contacts = new ArrayList<>();
        }
        this.location = (VenueLocation)source.readValue(VenueLocation.class.getClassLoader());

        this.description = source.readString();
        this.isFavorite = source.readByte() == 1;
        this.distance = source.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeByte((byte) (verified ? 1 : 0));
        dest.writeString(name);
        dest.writeString(ratingColor);
        dest.writeString(url);
        dest.writeTypedList(photos);
        dest.writeInt(ratingSignals);
        dest.writeString(storeId);
        dest.writeDouble(rating);
        dest.writeTypedList(contacts);
        dest.writeValue(location);
        dest.writeString(description);
        dest.writeByte((byte) (this.isFavorite ? 1 : 0));
        dest.writeFloat(this.distance);



    }

    public static final Creator<Venue> CREATOR = new Creator<Venue>() {
        @Override
        public Venue createFromParcel(Parcel source) {
            return new Venue(source);
        }

        @Override
        public Venue[] newArray(int size) {
            return new Venue[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRatingColor() {
        return ratingColor;
    }

    public void setRatingColor(String ratingColor) {
        this.ratingColor = ratingColor;
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<VenueImage> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<VenueImage> photos) {
        this.photos = photos;
    }

    public int getRatingSignals() {
        return ratingSignals;
    }

    public void setRatingSignals(int ratingSignals) {
        this.ratingSignals = ratingSignals;
    }


    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }


    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }



    @Override
    public String toString() {
        return "Id: " + id + "\n" +
                "Title: " + name + "\n" +
                "Duration: " + ratingSignals + "\n" +
                "Rating: " + rating + "\n" +
                "Poster: " + url + "\n" +
                "Overview: " + storeId + "\n" ;
    }
}
