package com.lab7.server.command;

import com.lab7.common.util.Request;
import com.lab7.common.util.CommandResponse;
import com.lab7.server.CollectionManager;
import com.lab7.server.database.DBManager;

public class RemoveLowerCommand extends AbstractCommand {
    private final DBManager dbManager;
    private final CollectionManager collectionManager;

    public RemoveLowerCommand(DBManager dbManager, CollectionManager collectionManager) {
        super("удалить из коллекции все элементы, меньшие, чем заданный", true);
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(Request request) {
        String clientName = request.getClientName();
        double distance = request.getCommandArgument().doubleValue();
        if (dbManager.deleteRoutesLowerThanDistance(clientName, distance)) {
            return new CommandResponse("Было удалено " + collectionManager.removeLower(clientName, distance) + " элементов");
        }
        return new CommandResponse("Ошибка при удалении элементов из базы данных");
    }
}
