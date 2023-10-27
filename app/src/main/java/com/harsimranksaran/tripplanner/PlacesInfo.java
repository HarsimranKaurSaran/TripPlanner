package com.harsimranksaran.tripplanner;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public class PlacesInfo {

    private String name;
    private String address;
    private String attributions;
    private String phoneNumber;
    private String id;
    private Uri websiteUri;
    private LatLng latLng;
    private float rating;

    public PlacesInfo(){

    }

    public PlacesInfo( String id, String name, String address, String attributions, String phoneNumber, Uri websiteUri, LatLng latLng, float rating) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.attributions = attributions;
        this.phoneNumber = phoneNumber;
        this.websiteUri = websiteUri;
        this.latLng = latLng;
        this.rating = rating;
    }

    public PlacesInfo(String string, String string1, String string2) {
        this.id = string;
        this.name = string1;
        this.address = string2;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getAttributions() {
        return attributions;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getId() {
        return id;
    }

    public Uri getWebsiteUri() {
        return websiteUri;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public float getRating() {
        return rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAttributions(String attributions) {
        this.attributions = attributions;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setWebsiteUri(Uri websiteUri) {
        this.websiteUri = websiteUri;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "PlacesInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", attributions='" + attributions + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", id='" + id + '\'' +
                ", websiteUri=" + websiteUri +
                ", latLng=" + latLng +
                ", rating=" + rating +
                '}';
    }
}
