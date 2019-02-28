package com.example.appejmjson.model;

public class City {

    private String country;
    private String name;
    private double latitude;
    private double longitude;

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public City(String country, String name, double latitude, double longitude) {
        this.country = country;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String toJsonString() {
        return "{ \"country\": \"ES\",\n" +
                "       \"name\": \"Valencia\", \n" +
                "       \"latitude\": 39..46666667, \n" +
                "       \"longitude\": -.366667} ";
    }
}
