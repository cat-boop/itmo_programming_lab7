package com.lab7.common.util;

import com.lab7.common.entity.Route;
import com.lab7.common.interfaces.IResponse;

import java.util.TreeSet;
import java.util.stream.Collectors;

public class CommandResponse implements IResponse {
    private String serverMessage;
    private TreeSet<Route> collection;

    public CommandResponse(String serverMessage) {
        this.serverMessage = serverMessage;
    }

    public CommandResponse(TreeSet<Route> collection) {
        this.collection = collection;
    }

    @Override
    public String toString() {
        return (collection == null ? serverMessage : collection.isEmpty() ? "Коллекция пуста" : collection.stream().map(Route::toString).collect(Collectors.joining("\n")));
    }
}
