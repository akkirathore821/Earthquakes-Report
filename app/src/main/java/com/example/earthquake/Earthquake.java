package com.example.earthquake;

public class Earthquake {

    double  magnitude;
    String place;
    long time;
    String url ;
    double latitude;
    double longitude;
    double depth;

    public Earthquake(double magnitude, String place, long time, String url, double latitude, double longitude, double depth) {
        this.magnitude = magnitude;
        this.place = place;
        this.time = time;
        this.url = url;
        this.latitude = latitude;
        this.longitude = longitude;
        this.depth = depth;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }
}
