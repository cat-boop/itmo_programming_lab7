package com.lab7.server.command;

import com.lab7.common.entity.Route;
import com.lab7.common.util.Request;
import com.lab7.common.util.CommandResponse;
import com.lab7.server.CollectionManager;
import com.lab7.server.database.DBManager;

public class AddCommand extends AbstractCommand {
    private final DBManager dbManager;
    private final CollectionManager collectionManager;

    public AddCommand(DBManager dbManager, CollectionManager collectionManager) {
        super("добавить новый элемент в коллекцию", true);
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(Request request) {
        Route route = request.getRouteToSend();
        long nextId = dbManager.addRouteToDB(route);
        if (nextId != -1) {
            route.setId(nextId);
            collectionManager.add(route);
            return new CommandResponse("Элемент успешно добавлен");
        }
        return new CommandResponse("Ошибка при добавлении элемента, возможно, такой элемент уже существует");
    }
}
