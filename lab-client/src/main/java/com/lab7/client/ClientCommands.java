package com.lab7.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ClientCommands {
    private static List<String> availableCommands;
    private static List<String> commandsNeedRoute;
    private static Map<String, Class<?>> commandsNeedArgument;

    static {
        availableCommands = new ArrayList<>();
        availableCommands.add("add");
        availableCommands.add("add_if_min");
        availableCommands.add("clear");
        availableCommands.add("count_greater_than_distance");
        availableCommands.add("count_less_than_distance");
        availableCommands.add("help");
        availableCommands.add("info");
        availableCommands.add("max_by_distance");
        availableCommands.add("remove_by_id");
        availableCommands.add("remove_greater");
        availableCommands.add("remove_lower");
        availableCommands.add("show");
        availableCommands.add("update");
        availableCommands.add("execute_script");
        availableCommands.add("exit");

        commandsNeedRoute = new ArrayList<>();
        commandsNeedRoute.add("add");
        commandsNeedRoute.add("update");
        commandsNeedRoute.add("add_if_min");
        commandsNeedRoute.add("remove_greater");
        commandsNeedRoute.add("remove_lower");

        commandsNeedArgument = new HashMap<>();
        commandsNeedArgument.put("update", Long.class);
        commandsNeedArgument.put("remove_by_id", Long.class);
        commandsNeedArgument.put("execute_script", String.class);
        commandsNeedArgument.put("count_less_than_distance", Double.class);
        commandsNeedArgument.put("count_greater_than_distance", Double.class);
    }

    private ClientCommands() {
    }

    public static List<String> getAvailableCommands() {
        return availableCommands;
    }

    public static List<String> getCommandsNeedRoute() {
        return commandsNeedRoute;
    }

    public static Map<String, Class<?>> getCommandsNeedArgument() {
        return commandsNeedArgument;
    }
}
