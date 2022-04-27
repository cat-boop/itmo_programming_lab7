package com.lab7.server.commands;

import com.lab7.data.Route;
import com.lab7.common.util.Request;
import com.lab7.common.util.Response;
import com.lab7.server.CollectionManager;
import com.lab7.server.database.DBManager;

import java.util.NoSuchElementException;

public class UpdateCommand extends AbstractCommand {
    private final DBManager dbManager;
    private final CollectionManager collectionManager;

    public UpdateCommand(DBManager dbManager, CollectionManager collectionManager) {
        super("обновить значение элемента коллекции, id которого равен заданному", false);
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        if (!dbManager.getConnectedClients().contains(request.getClientName())) {
            return new Response("Клиент с таким именем не подключен");
        }
        Response responseToReturn;
        long id = request.getCommandArgument().longValue();
        try {
            Route route = collectionManager.getRouteById(id);
            if (!route.getClientName().equals(request.getClientName())) {
                responseToReturn = new Response("Вы не владелец данного элемента, поэтому вы не можете обновить его в коллекции");
            } else if (dbManager.updateRouteByID(route)) {
                collectionManager.updateById(id, route);
                responseToReturn = new Response("Элемент с id = " + id + " удален из коллекции");
            } else {
                responseToReturn = new Response("Ошибка при удалении из базы данных");
            }
        } catch (NoSuchElementException e) {
            responseToReturn = new Response("Элемента с id = " + id + " не существует");
        }
        return responseToReturn;
    }
}
