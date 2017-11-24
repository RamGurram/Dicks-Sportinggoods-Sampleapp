package com.androidtask.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class VenueImage implements Parcelable{

    public VenueImage(String url, String photoId, int createdAt) {
        this.url = url;
        this.photoId = photoId;
        this.createdAt = createdAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    @Expose
    @SerializedName("url")
    private String url;

    @Expose
    @SerializedName("photoId")
    private String photoId;

    public int getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }

    @Expose
    @SerializedName("createdAt")
    private int createdAt;


    public VenueImage() {
    }


    private VenueImage(Parcel source) {
        url = source.readString();
        photoId = source.readString();
        createdAt = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(photoId);
        dest.writeInt(createdAt);
    }

    public static final Creator<VenueImage> CREATOR = new Creator<VenueImage>() {
        @Override
        public VenueImage createFromParcel(Parcel source) {
            return new VenueImage(source);
        }

        @Override
        public VenueImage[] newArray(int size) {
            return new VenueImage[size];
        }
    };

}
