package com.lab7.server.commands;

import com.lab7.common.util.Request;
import com.lab7.common.util.Response;
import com.lab7.server.CollectionManager;
import com.lab7.server.database.DBManager;

public class RemoveLowerCommand extends AbstractCommand {
    private final DBManager dbManager;
    private final CollectionManager collectionManager;

    public RemoveLowerCommand(DBManager dbManager, CollectionManager collectionManager) {
        super("удалить из коллекции все элементы, меньшие, чем заданный", false);
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        if (!dbManager.getConnectedClients().contains(request.getClientName())) {
            return new Response("Клиент с таким именем не подключен");
        }
        String clientName = request.getClientName();
        double distance = request.getCommandArgument().doubleValue();
        if (dbManager.deleteRoutesLowerThanDistance(clientName, distance)) {
            return new Response("Было удалено " + collectionManager.removeLower(clientName, distance) + " элементов");
        }
        return new Response("Ошибка при удалении элементов из базы данных");
    }
}
