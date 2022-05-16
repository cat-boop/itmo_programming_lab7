package com.lab7.server.command;

import com.lab7.common.entity.Route;
import com.lab7.common.util.Request;
import com.lab7.common.util.CommandResponse;
import com.lab7.server.CollectionManager;
import com.lab7.server.database.DBManager;

public class AddIfMinCommand extends AbstractCommand {
    private final DBManager dbManager;
    private final CollectionManager collectionManager;

    public AddIfMinCommand(DBManager dbManager, CollectionManager collectionManager) {
        super("добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции", true);
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(Request request) {
        Route route = request.getRouteToSend();
        if (collectionManager.getCollection().stream().allMatch((collectionRoute) -> Double.compare(route.getDistance(), collectionRoute.getDistance()) < 0)) {
            synchronized (dbManager) {
                long nextId = dbManager.addRouteToDB(route);
                if (nextId != -1) {
                    route.setId(nextId);
                    collectionManager.add(route);
                    return new CommandResponse("Элемент успешно добавлен");
                } else {
                    return new CommandResponse("Ошибка при добавлении в базу данных");
                }
            }
        }
        return new CommandResponse("Новый путь не меньше минимального");
    }
}
