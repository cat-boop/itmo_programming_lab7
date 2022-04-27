package com.lab7.server.commands;

import com.lab7.data.Route;
import com.lab7.common.util.Request;
import com.lab7.common.util.Response;
import com.lab7.server.CollectionManager;
import com.lab7.server.database.DBManager;

public class AddIfMinCommand extends AbstractCommand {
    private final DBManager dbManager;
    private final CollectionManager collectionManager;

    public AddIfMinCommand(DBManager dbManager, CollectionManager collectionManager) {
        super("добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции", false);
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        if (!dbManager.getConnectedClients().contains(request.getClientName())) {
            return new Response("Клиент с таким именем не подключен");
        }
        Route route = request.getRouteToSend();
        if (collectionManager.getCollection().stream().allMatch((collectionRoute) -> Double.compare(route.getDistance(), collectionRoute.getDistance()) < 0)) {
            long nextId = dbManager.addRouteToDB(route);
            if (nextId != -1) {
                route.setId(nextId);
                collectionManager.add(route);
                return new Response("Элемент успешно добавлен");
            } else {
                return new Response("Ошибка при добавлении в базу данных");
            }
        }
        return new Response("Новый путь не меньше минимального");
    }
}
