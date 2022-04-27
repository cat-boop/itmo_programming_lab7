package com.lab7.server.commands;

import com.lab7.common.util.Request;
import com.lab7.common.util.Response;
import com.lab7.server.CollectionManager;
import com.lab7.server.database.DBManager;

public class ClearCommand extends AbstractCommand {
    private final DBManager dbManager;
    private final CollectionManager collectionManager;

    public ClearCommand(DBManager dbManager, CollectionManager collectionManager) {
        super("очистить коллекцию", false);
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        if (!dbManager.getConnectedClients().contains(request.getClientName())) {
            return new Response("Клиент с таким именем не подключен");
        }
        if (dbManager.deleteClientRoutes(request.getClientName())) {
            collectionManager.clearClientRoutes(request.getClientName());
            return new Response("Коллекция успешно очищена");
        }
        return new Response("Ошибка при удалении из базы данных");
    }
}
