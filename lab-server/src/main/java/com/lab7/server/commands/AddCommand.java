package com.lab7.server.commands;

import com.lab7.data.Route;
import com.lab7.common.util.Request;
import com.lab7.common.util.Response;
import com.lab7.server.CollectionManager;
import com.lab7.server.database.DBManager;

public class AddCommand extends AbstractCommand {
    private final DBManager dbManager;
    private final CollectionManager collectionManager;

    public AddCommand(DBManager dbManager, CollectionManager collectionManager) {
        super("добавить новый элемент в коллекцию", false);
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        if (!dbManager.getConnectedClients().contains(request.getClientName())) {
            return new Response("Клиент с таким именем не подключен");
        }
        Route route = request.getRouteToSend();
        long nextId = dbManager.addRouteToDB(route);
        if (nextId != -1) {
            route.setId(nextId);
            collectionManager.add(route);
            return new Response("Элемент успешно добавлен");
        }
        return new Response("Ошибка при добавлении элемента, возможно, такой элемент уже существует");
    }
}
