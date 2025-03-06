
## Overview
This project implements an optimized delivery route planning system using Java. The system ensures that the delivery executive (Aman) takes the shortest path while considering food preparation time, minimizing waiting time, and efficiently delivering food to consumers.

## Features
- **Shortest Path Optimization**: Uses a priority queue to prioritize restaurant pickups.
- **Dynamic Scheduling**: If food is not ready, the delivery person can deliver pending orders instead of waiting.
- **Haversine Formula**: Calculates the shortest distance between locations using latitude and longitude.
- **Multithreading Support**: Uses an ExecutorService for potential performance improvements.

## Project Structure
```
org.example.service
    ├── BestRouteService.java  (Interface for route calculation)
    ├── BestRouteServiceImpl.java (Implementation of optimized route planning)
org.example.model
    ├── Location.java  (Abstract base class for locations)
    ├── Restaurant.java (Represents a restaurant location)
    ├── Consumer.java (Represents a consumer location)
```

## Detailed Code Explanation
### BestRouteServiceImpl.java

#### 1. **Package and Imports**
```java
package org.example.service;
import org.example.model.Location;
import org.example.model.Restaurant;
import org.example.model.Consumer;
import java.util.*;
import java.util.concurrent.*;
```
- Declares the package and imports necessary classes for collections and concurrency.

#### 2. **Class and Thread Pool Initialization**
```java
public class BestRouteServiceImpl implements BestRouteService {
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
```
- Implements `BestRouteService` and initializes a thread pool for potential parallel execution.

#### 3. **Find Best Route Method**
```java
@Override
public List<Location> findBestRoute(Location start, List<Location> locations) throws ExecutionException, InterruptedException {
```
- Computes the best route for food delivery based on the shortest time.

#### 4. **Data Structures**
```java
List<Location> bestRoute = new ArrayList<>();
PriorityQueue<Location> pickupQueue = new PriorityQueue<>(Comparator.comparingDouble(Location::getPreparationTime));
List<Location> deliveryQueue = new ArrayList<>();
```
- `pickupQueue`: Prioritizes restaurants based on food preparation time.
- `deliveryQueue`: Maintains the list of consumers waiting for delivery.

#### 5. **Sorting Restaurants by Preparation Time**
```java
for (Location loc : locations) {
    if (loc instanceof Restaurant) {
        pickupQueue.add(loc);
    }
}
```
- Adds only restaurant locations to the priority queue.

#### 6. **Route Processing Loop**
```java
while (!pickupQueue.isEmpty() || !deliveryQueue.isEmpty()) {
```
- Keeps running until all pickups and deliveries are completed.

#### 7. **Handling Pickups**
```java
Location nextPickup = pickupQueue.peek();
if (nextPickup != null && (currentTime + calculateTravelTime(current, nextPickup)) >= nextPickup.getPreparationTime()) {
    pickupQueue.poll();
    bestRoute.add(nextPickup);
    deliveryQueue.add(findConsumerForRestaurant(locations, (Restaurant) nextPickup));
    currentTime += calculateTravelTime(current, nextPickup);
    current = nextPickup;
}
```
- Ensures food is ready before picking it up.

#### 8. **Handling Deliveries**
```java
else if (!deliveryQueue.isEmpty()) {
    Location nextDelivery = deliveryQueue.remove(0);
    bestRoute.add(nextDelivery);
    currentTime += calculateTravelTime(current, nextDelivery);
    current = nextDelivery;
}
```
- Delivers food if a consumer's order is ready.

#### 9. **Waiting Mechanism**
```java
else {
    currentTime++;
}
```
- Waits if no pickups or deliveries are possible.

#### 10. **Finding Consumers for Restaurants**
```java
private Location findConsumerForRestaurant(List<Location> locations, Restaurant restaurant) {
    for (Location loc : locations) {
        if (loc instanceof Consumer && ((Consumer) loc).getOrderedFrom().equals(restaurant.getName())) {
            return loc;
        }
    }
    return null;
}
```
- Matches consumers with their respective restaurant orders.

#### 11. **Haversine Distance Calculation**
```java
private double calculateTravelTime(Location current, Location next) {
    final double EARTH_RADIUS = 6371; // In KM
    double dLat = Math.toRadians(next.getLat() - current.getLat());
    double dLongt = Math.toRadians(next.getLongt() - current.getLongt());
    double lat1 = Math.toRadians(current.getLat());
    double lat2 = Math.toRadians(next.getLat());
    double aVal = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.sin(dLongt / 2) * Math.sin(dLongt / 2) * Math.cos(lat1) * Math.cos(lat2);
    double c = 2 * Math.atan2(Math.sqrt(aVal), Math.sqrt(1 - aVal));
    return (EARTH_RADIUS * c) / 20.0;  // Assuming an average speed of 20 Km/hr
}
```
- Uses the Haversine formula to estimate travel time based on real-world distances.

#### 12. **Shutdown Executor**
```java
executor.shutdown();
```
- Closes the thread pool after execution.

## How to Run
1. Clone the repository.
2. Ensure Java 11 or later is installed.
3. Compile and run the `BestRouteServiceImpl` class.
4. Provide a list of `Location` objects representing restaurants and consumers.
5. Observe the optimized delivery route.

## Conclusion
This project optimizes the food delivery route considering:
- **Shortest travel time**
- **Food preparation time**
- **Parallel execution**
- **Efficient pickup and delivery scheduling**

