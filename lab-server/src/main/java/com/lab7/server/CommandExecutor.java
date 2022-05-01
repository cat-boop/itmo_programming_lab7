package com.lab7.server;

import com.lab7.common.interfaces.IResponse;
import com.lab7.common.util.CommandResponse;
import com.lab7.common.util.Request;
import com.lab7.server.command.AbstractCommand;

import java.util.concurrent.RecursiveTask;

public class CommandExecutor extends RecursiveTask<IResponse> {
    private final CommandManager commandManager;
    private final Request request;

    public CommandExecutor(CommandManager commandManager, Request request) {
        this.commandManager = commandManager;
        this.request = request;
    }

    @Override
    protected IResponse compute() {
        AbstractCommand command = commandManager.getCommandByName(request.getCommandName());
        if (command.needCheckAuthentication()) {
            if (!commandManager.getDbManager().checkIfUserConnect(request)) {
                return new CommandResponse("Вы не авторизованы");
            }
        }
        return command.execute(request);
    }
}
