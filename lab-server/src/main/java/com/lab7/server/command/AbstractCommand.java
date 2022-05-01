package com.lab7.server.command;

import com.lab7.common.interfaces.IResponse;
import com.lab7.common.util.Request;

public abstract class AbstractCommand {
    private final String commandDescription;
    private final boolean needCheckAuthentication;

    AbstractCommand(String commandDescription, boolean needCheckAuthentication) {
        this.commandDescription = commandDescription;
        this.needCheckAuthentication = needCheckAuthentication;
    }

    public String getCommandDescription() {
        return commandDescription;
    }

    public boolean needCheckAuthentication() {
        return needCheckAuthentication;
    }

    public abstract IResponse execute(Request request);
}
