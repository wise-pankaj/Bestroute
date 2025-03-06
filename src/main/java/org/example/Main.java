package org.example;

import org.example.model.*;
import org.example.service.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        DeliveryBoy aman = new DeliveryBoy("Aman", 12.935, 77.611);
        Restaurant r1 = new Restaurant("R1", 12.936, 77.612, 10);
        Restaurant r2 = new Restaurant("R2", 13.937, 77.613, 5);
        Consumer c1 = new Consumer("C1", 12.938, 77.614, "R1");
        Consumer c2 = new Consumer("C2", 12.939, 77.615, "R2");

        List<Location> locations = Arrays.asList(r1, r2, c1, c2);
        BestRouteServiceImpl optimizer = new BestRouteServiceImpl();

        List<Location> bestRoute = optimizer.findBestRoute(aman, locations);
        System.out.println("Best Route: " + bestRoute.stream().map(Location::getName).toList());
    }
}
