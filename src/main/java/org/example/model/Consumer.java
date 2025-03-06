package org.example.model;

public class Consumer extends Location {
    private String orderedFrom;

    public Consumer(String name, double lat, double longt, String orderedFrom) {
        super(name, lat, longt);
        this.orderedFrom = orderedFrom;
    }

    public String getOrderedFrom() { return orderedFrom; }

    @Override
    public double getPreparationTime() {
        return 0;
    }
}
