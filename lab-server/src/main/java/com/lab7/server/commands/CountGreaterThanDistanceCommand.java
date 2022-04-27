package com.lab7.server.commands;

import com.lab7.common.util.Request;
import com.lab7.common.util.Response;
import com.lab7.server.CollectionManager;
import com.lab7.server.database.DBManager;

public class CountGreaterThanDistanceCommand extends AbstractCommand {
    private final DBManager dbManager;
    private final CollectionManager collectionManager;

    public CountGreaterThanDistanceCommand(DBManager dbManager, CollectionManager collectionManager) {
        super("вывести количество элементов, значение поля distance которых больше заданного", false);
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        if (!dbManager.getConnectedClients().contains(request.getClientName())) {
            return new Response("Клиент с таким именем не подключен");
        }
        double distance = request.getCommandArgument().doubleValue();
        return new Response("Количество маршрутов с протяженностью больше чем " + distance
                + " равно " + collectionManager.countGreaterThanDistance(distance));

    }
}
