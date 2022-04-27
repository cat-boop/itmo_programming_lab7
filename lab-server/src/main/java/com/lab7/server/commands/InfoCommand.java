package com.lab7.server.commands;

import com.lab7.common.util.Request;
import com.lab7.common.util.Response;
import com.lab7.server.CollectionManager;
import com.lab7.server.database.DBManager;

public class InfoCommand extends AbstractCommand {
    private final DBManager dbManager;
    private final CollectionManager collectionManager;

    public InfoCommand(DBManager dbManager, CollectionManager collectionManager) {
        super("вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)", false);
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        if (!dbManager.getConnectedClients().contains(request.getClientName())) {
            return new Response("Клиент с таким именем не подключен");
        }
        return new Response("Тип коллекции - " + collectionManager.getCollectionName() + "\n"
                + "Количество элементов - " + collectionManager.getSize() + "\n"
                + "Дата инициализации - " + collectionManager.getCreationDate());
    }
}
