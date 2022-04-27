package com.lab7.server.commands;

import com.lab7.common.util.Request;
import com.lab7.common.util.Response;
import com.lab7.server.database.DBManager;

public class DisconnectCommand extends AbstractCommand {
    private final DBManager dbManager;

    public DisconnectCommand(DBManager dbManager) {
        super("Отключиться от базы данных", false);
        this.dbManager = dbManager;
    }

    @Override
    public Response execute(Request request) {
        if (dbManager.disconnectClient(request.getClientName())) {
            System.out.println("Клиент " + request.getClientName() + " отключился");
            return new Response("Успешно отключился от базы данных", false);
        }
        return new Response("Вы не были подключены к базе данных", false);
    }
}
