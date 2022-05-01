package com.lab7.server.command;

import com.lab7.common.util.Request;
import com.lab7.common.util.CommandResponse;
import com.lab7.server.CollectionManager;

public class ShowCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public ShowCommand(CollectionManager collectionManager) {
        super("вывести в стандартный поток вывода все элементы коллекции в строковом представлении", true);
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(Request request) {
        return new CommandResponse(collectionManager.getCollection());
    }
}
