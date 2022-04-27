package com.lab7.server.commands;

import com.lab7.common.util.Request;
import com.lab7.common.util.Response;
import com.lab7.server.database.DBManager;

public class ConnectToDBCommand extends AbstractCommand {
    private final DBManager dbManager;

    public ConnectToDBCommand(DBManager dbManager) {
        super("проверяет правильность имени и пароли клиента, если такого клиента не существует, создается новый", false);
        this.dbManager = dbManager;
    }

    @Override
    public Response execute(Request request) {
        if (dbManager.checkIfUserExist(request.getClientName())) {
            //System.out.println("Пользователь с таким именем существует");
            if (dbManager.checkIfUserConnect(request)) {
                dbManager.addConnectedClient(request.getClientName());
                System.out.println("Клиент " + request.getClientName() + " подключился");
                return new Response("Клиент успешно подключился", true);
            } else {
                return new Response("Ошибка при вводе пароля", false);
            }
        }
        System.out.println("Пользователя с таким именем не существует, пытаюсь зарегистрировать нового");
        if (dbManager.registerNewUser(request)) {
            System.out.println("Клиент " + request.getClientName() + " подключился");
            return new Response("Новый клиент успешно зарегистрирован", true);
        }
        return new Response("Непредвиденная ошибка", false);
    }
}
