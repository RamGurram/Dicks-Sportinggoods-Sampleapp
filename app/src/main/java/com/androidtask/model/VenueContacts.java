package com.androidtask.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VenueContacts implements Parcelable {


    @Expose
    @SerializedName("phone")
    private String phone;

    public String getTwitter() {
        return twitter;
    }

    @Expose
    @SerializedName("twitter")
    private String twitter;

    @Expose
    @SerializedName("facebook")
    private String facebook;

    @Expose
    @SerializedName("facebookName")
    private String facebookName;

    public VenueContacts() {}

    public VenueContacts(Parcel source) {
        this.phone = source.readString();
        this.twitter = source.readString();
        this.facebook = source.readString();
        this.facebookName = source.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(phone);
        dest.writeString(twitter);
        dest.writeString(facebook);
        dest.writeString(facebookName);

    }

    public static final Parcelable.Creator<VenueContacts> CREATOR = new Creator<VenueContacts>() {
        @Override
        public VenueContacts createFromParcel(Parcel source) {
            return new VenueContacts(source);
        }

        @Override
        public VenueContacts[] newArray(int size) {
            return new VenueContacts[size];
        }
    };




    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getFacebookName() {
        return facebookName;
    }

    public void setFacebookName(String facebookName) {
        this.facebookName = facebookName;
    }
}
