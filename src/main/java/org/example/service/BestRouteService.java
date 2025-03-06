package org.example.service;
import org.example.model.Location;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface BestRouteService {
    List<Location> findBestRoute(Location start, List<Location> locations) throws ExecutionException, InterruptedException;
}
