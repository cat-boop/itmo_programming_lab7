package com.lab7.server.command;

import com.lab7.common.entity.Route;
import com.lab7.common.util.Request;
import com.lab7.common.util.CommandResponse;
import com.lab7.server.CollectionManager;

import java.util.NoSuchElementException;

public class MaxByDistanceCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public MaxByDistanceCommand(CollectionManager collectionManager) {
        super("вывести любой объект из коллекции, значение поля distance которого является максимальным", true);
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(Request request) {
        try {
            Route route = collectionManager.maxByDistance();
            return new CommandResponse(route.toString());
        } catch (NoSuchElementException e) {
            return new CommandResponse("Коллекция пуста");
        }
    }
}
