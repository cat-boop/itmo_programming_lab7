package com.lab7.server.command;

import com.lab7.common.util.Request;
import com.lab7.common.util.CommandResponse;
import com.lab7.server.CollectionManager;

public class CountLessThanDistanceCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public CountLessThanDistanceCommand(CollectionManager collectionManager) {
        super("вывести количество элементов, значение поля distance которых меньше заданного", true);
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(Request request) {
        double distance = request.getCommandArgument().doubleValue();
        return new CommandResponse("Количество маршрутов с протяженностью меньше чем " + distance
                + " равно " + collectionManager.countLessThanDistance(distance));
    }
}
