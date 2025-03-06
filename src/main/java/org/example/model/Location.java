package org.example.model;

public abstract class Location {
    private String name;
    private double lat;
    private double longt;

    public Location(String name, double lat, double longt) {
        this.name = name;
        this.lat = lat;
        this.longt = longt;
    }

    public String getName() { return name; }
    public double getLat() { return lat; }
    public double getLongt() { return longt; }
    public abstract double getPreparationTime();
}
