package com.lab7.server.commands;

import com.lab7.common.util.IResponse;
import com.lab7.common.util.Request;

public abstract class AbstractCommand {
    private final String commandDescription;
    private final boolean isServerCommand;

    AbstractCommand(String commandDescription, boolean isServerCommand) {
        this.commandDescription = commandDescription;
        this.isServerCommand = isServerCommand;
    }

    public String getCommandDescription() {
        return commandDescription;
    }

    public boolean isServerCommand() {
        return isServerCommand;
    }

    public abstract IResponse execute(Request request);
}
