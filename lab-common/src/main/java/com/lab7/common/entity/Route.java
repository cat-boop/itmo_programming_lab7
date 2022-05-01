package com.lab7.common.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Main class that stored in collection
 */
public class Route implements Serializable, Comparable<Route> {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String clientName;
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Location from; //Поле не может быть null
    private Location to; //Поле может быть null
    private double distance; //Значение поля должно быть больше 1

    public Route(String name, Coordinates coordinates, Location from, Location to, double distance) {
        creationDate = LocalDateTime.now();
        this.name = name;
        this.coordinates = coordinates;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public Route() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public void setTo(Location to) {
        this.to = to;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * @return id of Route
     */
    public Long getId() {
        return id;
    }

    public String getClientName() {
        return clientName;
    }

    /**
     * @return name of Route
     */
    public String getName() {
        return name;
    }

    /**
     * @return coordinates of Route
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * @return creation date of Route
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * @return location where Route starts
     */
    public Location getFrom() {
        return from;
    }

    /**
     * @return location where Route ends
     */
    public Location getTo() {
        return to;
    }

    /**
     * @return distance of Route
     */
    public double getDistance() {
        return distance;
    }

    /**
     * replace current route by new information
     * @param route route with new information
     */
    public void update(Route route) {
        name = route.name;
        coordinates = route.coordinates;
        from = route.from;
        to = route.to;
        distance = route.distance;
    }

    /**
     * @return route represented by beautiful string
     */
    @Override
    public String toString() {
        return "{Id = " + id + ", client name = \"" + clientName + "\", name = \"" + name + "\", coordinates = " + coordinates + ", creation date = "
                + creationDate.toString() + ", from = " + from + ", to = " + to + ", distance = " + distance + "}";
    }

    @Override
    public int compareTo(Route o) {
        return this.getName().compareTo(o.getName());
    }
}
