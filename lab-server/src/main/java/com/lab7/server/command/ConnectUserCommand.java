package com.lab7.server.command;

import com.lab7.common.util.ConnectResponse;
import com.lab7.common.util.Request;
import com.lab7.server.database.DBManager;
import com.lab7.server.logger.ServerLogger;

public class ConnectToDBCommand extends AbstractCommand {
    private final DBManager dbManager;

    public ConnectToDBCommand(DBManager dbManager) {
        super("проверяет правильность имени и пароли клиента, если такого клиента не существует, создается новый", false);
        this.dbManager = dbManager;
    }

    @Override
    public ConnectResponse execute(Request request) {
        if (dbManager.checkIfUserExist(request.getClientName())) {
            ServerLogger.logDebugMessage("Такой пользователь существует, проверка пароля");
            if (dbManager.checkIfUserConnect(request)) {
                System.out.println("Клиент " + request.getClientName() + " подключился");
                return new ConnectResponse("Клиент успешно подключился", true);
            } else {
                return new ConnectResponse("Ошибка при вводе пароля", false);
            }
        }
        ServerLogger.logDebugMessage("Пользователя с таким именем не существует, пытаюсь зарегистрировать нового");
        if (dbManager.registerNewUser(request)) {
            System.out.println("Клиент " + request.getClientName() + " подключился");
            return new ConnectResponse("Новый клиент успешно зарегистрирован", true);
        }
        return new ConnectResponse("Непредвиденная ошибка", false);
    }
}
