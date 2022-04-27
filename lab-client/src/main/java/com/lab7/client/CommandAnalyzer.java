package com.lab7.client;

public class CommandAnalyzer {
    private String commandName;
    private String commandArgument;
    private boolean commandHaveArgument;
    private boolean commandNeedRoute;
    private boolean commandScript;
    private boolean commandExit;
    private boolean commandConnectToDB;

    public CommandAnalyzer(String command) {
        commandHaveArgument = false;
        commandNeedRoute = false;
        commandScript = false;
        commandExit = false;
        commandConnectToDB = false;

        analyzeCommand(command);
    }

    public boolean commandIsScript() {
        return commandScript;
    }

    public boolean commandIsExit() {
        return commandExit;
    }

    public boolean commandIsConnectToDB() {
        return commandConnectToDB;
    }

    public boolean isCommandHaveArgument() {
        return commandHaveArgument;
    }

    public boolean isCommandNeedRoute() {
        return commandNeedRoute;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getCommandArgument() {
        return commandArgument;
    }

    public Class<?> getArgumentClass() {
        return ClientCommands.getCommandsNeedArgument().get(commandName);
    }

    private void analyzeCommand(String command) {
        String[] inputLineDivided = command.trim().split(" ", 2);
        commandName = inputLineDivided[0].toLowerCase();

        if (!ClientCommands.getAvailableCommands().contains(commandName)) {
            throw new IllegalStateException("Такой команды не существует");
        }
        if (ClientCommands.getCommandsNeedArgument().containsKey(commandName) && inputLineDivided.length == 1) {
            throw new IllegalStateException("Аргумент не указан");
        }
        if (!ClientCommands.getCommandsNeedArgument().containsKey(commandName) && inputLineDivided.length == 2) {
            throw new IllegalStateException("Аргумент не должен быть указан");
        }

        if ("execute_script".equals(commandName)) {
            commandScript = true;
        }
        if ("connect_to_db".equals(commandName)) {
            commandConnectToDB = true;
        }
        if ("exit".equals(commandName)) {
            commandExit = true;
        }
        if (ClientCommands.getCommandsNeedArgument().containsKey(commandName)) {
            commandHaveArgument = true;
            commandArgument = inputLineDivided[1];
        }
        if (ClientCommands.getCommandsNeedRoute().contains(commandName)) {
            commandNeedRoute = true;
        }
    }
}
