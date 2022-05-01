package com.lab7.server.command;

import com.lab7.common.util.Request;
import com.lab7.common.util.CommandResponse;
import com.lab7.server.CommandManager;

import java.util.stream.Collectors;

public class HelpCommand extends AbstractCommand {

    public HelpCommand() {
        super("вывести справку по доступным командам", false);
    }

    @Override
    public CommandResponse execute(Request request) {
        return new CommandResponse(CommandManager.getCommands().entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue().getCommandDescription()).collect(Collectors.joining("\n")));
    }
}
