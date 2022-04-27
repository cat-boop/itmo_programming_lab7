package com.lab7.server.commands;

import com.lab7.common.util.Request;
import com.lab7.common.util.Response;
import com.lab7.server.CommandManager;
import com.lab7.server.database.DBManager;

import java.util.stream.Collectors;

public class HelpCommand extends AbstractCommand {
    private final DBManager dbManager;

    public HelpCommand(DBManager dbManager) {
        super("вывести справку по доступным командам", false);
        this.dbManager = dbManager;
    }

    @Override
    public Response execute(Request request) {
        if (!dbManager.getConnectedClients().contains(request.getClientName())) {
            return new Response("Клиент с таким именем не подключен");
        }
        return new Response(CommandManager.getCommands().entrySet().stream().filter(entry -> !entry.getValue().isServerCommand()).map(entry -> entry.getKey() + ": " + entry.getValue().getCommandDescription()).collect(Collectors.joining("\n")));
    }
}
