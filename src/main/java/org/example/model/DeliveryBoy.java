package org.example.model;

public class DeliveryBoy extends Location{
    public DeliveryBoy(String name, double lat, double longt) {
        super(name, lat, longt);
    }

    @Override
    public double getPreparationTime() {
        return 0;
    }
}
