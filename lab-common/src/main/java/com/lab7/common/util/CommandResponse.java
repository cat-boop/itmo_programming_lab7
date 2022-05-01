package com.lab7.common.util;

import com.lab7.data.Route;

import java.io.Serializable;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Response implements Serializable {
    private String serverMessage;
    private TreeSet<Route> collection;
    private boolean clientConnected;

    public Response(String serverMessage) {
        this.serverMessage = serverMessage;
        clientConnected = true;
    }

    //TODO this bad?
    public Response(String serverMessage, boolean clientConnected) {
        this.serverMessage = serverMessage;
        this.clientConnected = clientConnected;
    }

    public Response(TreeSet<Route> collection) {
        this.collection = collection;
        clientConnected = true;
    }

    public boolean isClientConnected() {
        return clientConnected;
    }

    @Override
    public String toString() {
        return (collection == null ? serverMessage : collection.isEmpty() ? "Коллекция пуста" : collection.stream().map(Route::toString).collect(Collectors.joining("\n")));
    }
}
