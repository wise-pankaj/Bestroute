package org.example.service;

import org.example.model.Location;
import org.example.model.Restaurant;
import org.example.model.Consumer;

import java.util.*;
import java.util.concurrent.*;

public class BestRouteServiceImpl implements BestRouteService {
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public List<Location> findBestRoute(Location start, List<Location> locations) throws ExecutionException, InterruptedException {
        List<Location> bestRoute = new ArrayList<>();
        PriorityQueue<Location> pickupQueue = new PriorityQueue<>(Comparator.comparingDouble(Location::getPreparationTime));
        List<Location> deliveryQueue = new ArrayList<>();

        for (Location loc : locations) {
            if (loc instanceof Restaurant) {
                pickupQueue.add(loc);
            }
        }

        Location current = start;
        double currentTime = 0;

        while (!pickupQueue.isEmpty() || !deliveryQueue.isEmpty()) {
            Location nextPickup = pickupQueue.peek();
            if (nextPickup != null && (currentTime + calculateTravelTime(current, nextPickup)) >= nextPickup.getPreparationTime()) {
                pickupQueue.poll();
                bestRoute.add(nextPickup);
                deliveryQueue.add(findConsumerForRestaurant(locations, (Restaurant) nextPickup));
                currentTime += calculateTravelTime(current, nextPickup);
                current = nextPickup;
            } else if (!deliveryQueue.isEmpty()) {
                Location nextDelivery = deliveryQueue.remove(0);
                bestRoute.add(nextDelivery);
                currentTime += calculateTravelTime(current, nextDelivery);
                current = nextDelivery;
            } else {
                currentTime++; // If waiting is necessary
            }
        }

        executor.shutdown();
        return bestRoute;
    }

    private Location findConsumerForRestaurant(List<Location> locations, Restaurant restaurant) {
        for (Location loc : locations) {
            if (loc instanceof Consumer && ((Consumer) loc).getOrderedFrom().equals(restaurant.getName())) {
                return loc;
            }
        }
        return null;
    }

    private double calculateTravelTime(Location current, Location next) {
        final double EARTH_RADIUS = 6371; // In KM
        double dLat = Math.toRadians(next.getLat() - current.getLat());
        double dLongt = Math.toRadians(next.getLongt() - current.getLongt());
        double lat1 = Math.toRadians(current.getLat());
        double lat2 = Math.toRadians(next.getLat());

        double aVal = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLongt / 2) * Math.sin(dLongt / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(aVal), Math.sqrt(1 - aVal));
        return (EARTH_RADIUS * c) / 20.0;  // 20 Km/hr
    }
}
