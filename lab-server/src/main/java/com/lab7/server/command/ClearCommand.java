package com.lab7.server.command;

import com.lab7.common.util.Request;
import com.lab7.common.util.CommandResponse;
import com.lab7.server.CollectionManager;
import com.lab7.server.database.DBManager;

public class ClearCommand extends AbstractCommand {
    private final DBManager dbManager;
    private final CollectionManager collectionManager;

    public ClearCommand(DBManager dbManager, CollectionManager collectionManager) {
        super("очистить коллекцию", true);
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(Request request) {
        if (dbManager.deleteClientRoutes(request.getClientName())) {
            collectionManager.clearClientRoutes(request.getClientName());
            return new CommandResponse("Из коллекции удалены все ваши объекты");
        }
        return new CommandResponse("Ошибка при удалении из базы данных");
    }
}
