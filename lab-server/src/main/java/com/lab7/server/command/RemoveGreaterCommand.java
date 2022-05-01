package com.lab7.server.command;

import com.lab7.common.util.Request;
import com.lab7.common.util.CommandResponse;
import com.lab7.server.CollectionManager;
import com.lab7.server.database.DBManager;

public class RemoveGreaterCommand extends AbstractCommand {
    private final DBManager dbManager;
    private final CollectionManager collectionManager;

    public RemoveGreaterCommand(DBManager dbManager, CollectionManager collectionManager) {
        super("удалить из коллекции все элементы, превышающие заданный", true);
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(Request request) {
        String clientName = request.getClientName();
        double distance = request.getCommandArgument().doubleValue();
        if (dbManager.deleteRoutesGreaterThanDistance(clientName, distance)) {
            return new CommandResponse("Было удалено " + collectionManager.removeGreater(clientName, distance) + " элементов");
        }
        return new CommandResponse("Ошибка при удалении элементов из базы данных");
    }
}
