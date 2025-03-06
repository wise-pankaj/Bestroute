package org.example.model;

public class Restaurant extends Location {
    private double preparationTime;

    public Restaurant(String name, double lat, double longt, double preparationTime) {
        super(name, lat, longt);
        this.preparationTime = preparationTime;
    }

    @Override
    public double getPreparationTime() {
        return preparationTime;
    }
}
