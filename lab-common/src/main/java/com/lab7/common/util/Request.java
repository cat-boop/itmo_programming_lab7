package com.lab7.common.util;

import com.lab7.common.entity.Route;

import java.io.Serializable;

public class Request implements Serializable {
    private final String commandName;
    private final String clientName;
    private final String clientPassword;
    private Number commandArgument;
    private Route routeToSend;

    public Request(String commandName, String clientName, String clientPassword) {
        this.commandName = commandName;
        this.clientName = clientName;
        this.clientPassword = clientPassword;
    }

    public void setCommandArgument(Number commandArgument) {
        this.commandArgument = commandArgument;
    }

    public void setRouteToSend(Route routeToSend) {
        this.routeToSend = routeToSend;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientPassword() {
        return clientPassword;
    }

    public String getCommandName() {
        return commandName;
    }

    public Number getCommandArgument() {
        return commandArgument;
    }

    public Route getRouteToSend() {
        return routeToSend;
    }

    public String toString() {
        return "[Имя команды = " + commandName
                + (commandArgument == null ? "" : "; Аргумент команды = " + commandArgument)
                + (routeToSend == null ? "" : "; Путь = " + routeToSend) + "]";
    }
}
