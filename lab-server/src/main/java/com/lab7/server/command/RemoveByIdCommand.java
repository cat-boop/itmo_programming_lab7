package com.lab7.server.commands;

import com.lab7.data.Route;
import com.lab7.common.util.Request;
import com.lab7.common.util.CommandResponse;
import com.lab7.server.CollectionManager;
import com.lab7.server.database.DBManager;

import java.util.NoSuchElementException;

public class RemoveByIdCommand extends AbstractCommand {
    private final DBManager dbManager;
    private final CollectionManager collectionManager;

    public RemoveByIdCommand(DBManager dbManager, CollectionManager collectionManager) {
        super("удалить элемент из коллекции по его id", false);
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(Request request) {
        if (!dbManager.getConnectedClients().contains(request.getClientName())) {
            return new CommandResponse("Клиент с таким именем не подключен");
        }
        CommandResponse responseToReturn;
        long id = request.getCommandArgument().longValue();
        try {
            Route route = collectionManager.getRouteById(id);
            if (!route.getClientName().equals(request.getClientName())) {
                responseToReturn = new CommandResponse("Вы не владелец данного элемента, поэтому вы не можете удалить его из коллекции");
            } else if (dbManager.deleteRouteByID(id)) {
                collectionManager.removeById(id);
                responseToReturn = new CommandResponse("Элемент с id = " + id + " удален из коллекции");
            } else {
                responseToReturn = new CommandResponse("Ошибка при удалении из базы данных");
            }
        } catch (NoSuchElementException e) {
            responseToReturn = new CommandResponse("Элемента с id = " + id + " не существует");
        }
        return responseToReturn;
    }
}
