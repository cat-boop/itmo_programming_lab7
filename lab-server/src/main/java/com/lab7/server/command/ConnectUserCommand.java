package com.lab7.server.command;

import com.lab7.common.util.ConnectResponse;
import com.lab7.common.util.Request;
import com.lab7.server.database.DBManager;

public class ConnectUserCommand extends AbstractCommand {
    private final DBManager dbManager;

    public ConnectUserCommand(DBManager dbManager) {
        super("подключает клиента к базе данных", false);
        this.dbManager = dbManager;
    }

    @Override
    public ConnectResponse execute(Request request) {
        synchronized (dbManager) {
            if (dbManager.checkIfUserExist(request.getClientName())) {
                if (dbManager.checkIfUserConnect(request)) {
                    System.out.println("Клиент " + request.getClientName() + " подключился");
                    return new ConnectResponse("Клиент успешно подключился", true);
                } else {
                    return new ConnectResponse("Ошибка при вводе пароля", false);
                }
            } else {
                return new ConnectResponse("Пользователя с таким именем не существует", false);
            }
        }
    }
}
