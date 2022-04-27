package com.lab7.server.commands;

import com.lab7.data.Route;
import com.lab7.common.util.Request;
import com.lab7.common.util.Response;
import com.lab7.server.CollectionManager;
import com.lab7.server.database.DBManager;

import java.util.NoSuchElementException;

public class MaxByDistanceCommand extends AbstractCommand {
    private final DBManager dbManager;
    private final CollectionManager collectionManager;

    public MaxByDistanceCommand(DBManager dbManager, CollectionManager collectionManager) {
        super("вывести любой объект из коллекции, значение поля distance которого является максимальным", false);
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        if (!dbManager.getConnectedClients().contains(request.getClientName())) {
            return new Response("Клиент с таким именем не подключен");
        }
        try {
            Route route = collectionManager.maxByDistance();
            return new Response(route.toString());
        } catch (NoSuchElementException e) {
            return new Response("Коллекция пуста");
        }
    }
}
