package com.lab7.server.commands;

import com.lab7.common.util.Request;
import com.lab7.common.util.CommandResponse;
import com.lab7.server.CollectionManager;
import com.lab7.server.database.DBManager;

public class CountLessThanDistanceCommand extends AbstractCommand {
    private final DBManager dbManager;
    private final CollectionManager collectionManager;

    public CountLessThanDistanceCommand(DBManager dbManager, CollectionManager collectionManager) {
        super("вывести количество элементов, значение поля distance которых меньше заданного", false);
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(Request request) {
        if (!dbManager.getConnectedClients().contains(request.getClientName())) {
            return new CommandResponse("Клиент с таким именем не подключен");
        }
        double distance = request.getCommandArgument().doubleValue();
        return new CommandResponse("Количество маршрутов с протяженностью меньше чем " + distance
                + " равно " + collectionManager.countLessThanDistance(distance));
    }
}
