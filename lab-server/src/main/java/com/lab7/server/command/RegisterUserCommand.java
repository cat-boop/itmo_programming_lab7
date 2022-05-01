package com.lab7.server.command;

import com.lab7.common.interfaces.IResponse;
import com.lab7.common.util.ConnectResponse;
import com.lab7.common.util.Request;
import com.lab7.server.database.DBManager;

public class RegisterUserCommand extends AbstractCommand {
    private final DBManager dbManager;

    public RegisterUserCommand(DBManager dbManager) {
        super("регистрирует нового пользователя", false);
        this.dbManager = dbManager;
    }

    @Override
    public IResponse execute(Request request) {
        if (dbManager.checkIfUserExist(request.getClientName())) {
            return new ConnectResponse("Пользователь с таким именем уже существует", false);
        }
        if (dbManager.registerNewUser(request)) {
            System.out.println("Клиент " + request.getClientName() + " подключился");
            return new ConnectResponse("Новый клиент зарегистрированы", true);
        }
        return new ConnectResponse("Непредвиденная ошибка", false);
    }
}
