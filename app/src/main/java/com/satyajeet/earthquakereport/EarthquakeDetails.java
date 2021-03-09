package com.satyajeet.earthquakereport;

public class EarthquakeDetails {

    private String magintude;
    private String primaryLocation;
    private String locationOffSet;
    private String date;
    private String time;
    private String url;


    public EarthquakeDetails(String magintude, String primaryLocation, String locationOffSet, String date, String time, String url) {
        this.magintude = magintude;
        this.primaryLocation = primaryLocation;
        this.locationOffSet = locationOffSet;
        this.date = date;
        this.time = time;
        this.url = url;
    }

    public String getMagintude() {
        return magintude;
    }


    public String getPrimaryLocation() {
        return primaryLocation;
    }



    public String getLocationOffSet() {
        return locationOffSet;
    }



    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }

}

