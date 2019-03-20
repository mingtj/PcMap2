package com.android.pc.map.bean;

public class Gps {

    private double latitude;
    private double longitude;
    private double telphone;

    public Gps(double mTel,double wgLat, double wgLon) {
        setTelphone(mTel);
        setLatitude(wgLat);
        setLongitude(wgLon);
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

    public double getTelphone() {
        return telphone;
    }

    public void setTelphone(double telphone) {
        this.telphone = telphone;
    }
}