package com.lab7.server;

import com.lab7.data.Route;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Class that manage collection
 */
public class CollectionManager {
//    private static Long nextId = 1L;
    private final NavigableSet<Route> routes;
    private final LocalDateTime creationDate;

    public CollectionManager(NavigableSet<Route> routes) {
        this.routes = new TreeSet<>();
        this.routes.addAll(routes);
//        if (routes.size() > 0) {
//            nextId = routes.stream().max(Comparator.comparing(Route::getId)).get().getId() + 1;
//        }
        creationDate = LocalDateTime.now();
    }

    /**
     * @return current collection
     */
    public TreeSet<Route> getCollection() {
        return new TreeSet<>(routes);
    }

    /**
     * @return collection creation date
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * @return collection name
     */
    public String getCollectionName() {
        return routes.getClass().toString();
    }

    /**
     * @return size of collection
     */
    public int getSize() {
        return routes.size();
    }

    /**
     * @param route new Route to add to collection
     * @return true if element successfully added to collection, else false
     */
    public boolean add(Route route) {
//        route.setId(nextId++);
        return routes.add(route);
    }

    /**
     * update Route with given id with the data from given route
     * @param id id of route to update
     * @param route route contains data to update
     */
    public void updateById(Long id, Route route) {
        Route routeToUpdate = routes.stream().filter(collectionRoute -> collectionRoute.getId().equals(id)).findAny().orElseThrow(NoSuchElementException::new);
        routeToUpdate.update(route);
    }

    /**
     * if collection contains element with given id, this element will be removed, else collection will not be changed
     */
    public void removeById(Long id) {
        routes.remove(getRouteById(id));
    }

    public Route getRouteById(Long id) {
        return routes.stream().filter(route -> route.getId().equals(id)).findAny().orElseThrow(NoSuchElementException::new);
    }

    /**
     * remove all elements from collection
     */
    public void clearClientRoutes(String clientName) {
        routes.removeIf(route -> route.getName().equals(clientName));
    }

    /**
     * remove all routes in collection which distance greater than distance of given route
     */
    public int removeGreater(String clientName, double distance) {
        Collection<Route> collection = routes.stream().filter(setRoute -> Double.compare(setRoute.getDistance(), distance) > 0 && setRoute.getClientName().equals(clientName)).collect(Collectors.toList());
        collection.forEach(routes::remove);
        return collection.size();
    }

    /**
     * remove all routes in collection which distance lower than distance of given route
     */
    public int removeLower(String clientName, double distance) {
        Collection<Route> collection = routes.stream().filter(setRoute -> Double.compare(setRoute.getDistance(), distance) < 0 && setRoute.getClientName().equals(clientName)).collect(Collectors.toList());
        collection.forEach(routes::remove);
        return collection.size();
    }

    /**
     * @return route from collection with maximum distance
     */
    public Route maxByDistance() {
        return routes.stream().max(Comparator.comparing(Route::getDistance)).orElseThrow(NoSuchElementException::new);
    }

    /**
     * @return number of routes which distance is lower than the given distance
     */
    public int countLessThanDistance(double distance) {
        return (int) routes.stream().filter(route -> Double.compare(route.getDistance(), distance) < 0).count();
    }

    /**
     * @return number of routes which distance is greater than the given distance
     */
    public int countGreaterThanDistance(double distance) {
        return (int) routes.stream().filter(route -> Double.compare(route.getDistance(), distance) > 0).count();
    }
}
