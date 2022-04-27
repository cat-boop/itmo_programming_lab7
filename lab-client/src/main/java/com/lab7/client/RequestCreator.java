package com.lab7.client;

import com.lab7.data.Route;
import com.lab7.common.util.Request;

public final class RequestCreator {
    private static String clientName;
    private static String clientPassword;

    private RequestCreator() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void setClientName(String clientName) {
        RequestCreator.clientName = clientName;
    }

    public static void setClientPassword(String clientPassword) {
        RequestCreator.clientPassword = clientPassword;
    }

    public static Request createRequest(String command, RouteReader routeReader) {
        CommandAnalyzer commandAnalyzer = new CommandAnalyzer(command);
        Request request = new Request(commandAnalyzer.getCommandName(), clientName, clientPassword);
        if (commandAnalyzer.isCommandHaveArgument()) {
            Class<?> argumentClass = commandAnalyzer.getArgumentClass();
            Number commandArgument = null;
            try {
                if (argumentClass.equals(Long.class)) {
                    commandArgument = Long.parseLong(commandAnalyzer.getCommandArgument());
                }
                if (argumentClass.equals(Double.class)) {
                    commandArgument = Double.parseDouble(commandAnalyzer.getCommandArgument());
                }
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Ошибка при вводе аргумента");
            }
            request.setCommandArgument(commandArgument);
        }
        if (commandAnalyzer.isCommandNeedRoute()) {
            Route route = routeReader.readRoute();
            route.setClientName(clientName);
            request.setRouteToSend(route);
        }
        return request;
    }
}
