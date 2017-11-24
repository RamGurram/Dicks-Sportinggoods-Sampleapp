package com.androidtask.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VenueLocation  implements Parcelable {

    @Expose
    @SerializedName("address")
    private String address;

    @Expose
    @SerializedName("cc")
    private String cc;

    @Expose
    @SerializedName("city")
    private String city;

    @Expose
    @SerializedName("state")
    private String state;

    @Expose
    @SerializedName("latitude")
    private double latitude;

    public String getCc() {
        return cc;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getAddress() {
        return address;
    }

    @Expose
    @SerializedName("longitude")
    private double longitude;

    @Expose
    @SerializedName("postalCode")
    private String postalCode;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Expose
    @SerializedName("country")
    private String country;

    public VenueLocation() {}

    private VenueLocation(Parcel source) {
        this.address = source.readString();
        this.latitude = source.readDouble();
        this.longitude = source.readDouble();
        this.postalCode = source.readString();
        this.cc = source.readString();
        this.city = source.readString();
        this.state = source.readString();
        this.country = source.readString();


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(postalCode);
        dest.writeString(cc);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(country);


    }

    public static final Parcelable.Creator<VenueLocation> CREATOR = new Creator<VenueLocation>() {
        @Override
        public VenueLocation createFromParcel(Parcel source) {
            return new VenueLocation(source);
        }

        @Override
        public VenueLocation[] newArray(int size) {
            return new VenueLocation[size];
        }
    };



}
