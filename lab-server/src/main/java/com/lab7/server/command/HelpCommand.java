package com.lab7.server.command;

import com.lab7.common.util.Request;
import com.lab7.common.util.CommandResponse;
import com.lab7.server.CommandManager;

import java.util.stream.Collectors;

public class HelpCommand extends AbstractCommand {
    private final CommandManager commandManager;

    public HelpCommand(CommandManager commandManager) {
        super("вывести справку по доступным командам", false);
        this.commandManager = commandManager;
    }

    @Override
    public CommandResponse execute(Request request) {
        return new CommandResponse(commandManager.getCommands().entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue().get().getCommandDescription()).collect(Collectors.joining("\n")));
    }
}
