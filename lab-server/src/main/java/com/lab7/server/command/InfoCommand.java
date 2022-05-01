package com.lab7.server.command;

import com.lab7.common.util.Request;
import com.lab7.common.util.CommandResponse;
import com.lab7.server.CollectionManager;

public class InfoCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)", true);
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(Request request) {
        return new CommandResponse("Тип коллекции - " + collectionManager.getCollectionName() + "\n"
                + "Количество элементов - " + collectionManager.getSize() + "\n"
                + "Дата инициализации - " + collectionManager.getCreationDate());
    }
}
