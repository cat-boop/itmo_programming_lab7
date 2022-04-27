package com.lab7.server;

import com.lab7.common.util.Request;
import com.lab7.common.util.Response;
import com.lab7.server.commands.AddCommand;
import com.lab7.server.commands.AddIfMinCommand;
import com.lab7.server.commands.ClearCommand;
import com.lab7.server.commands.AbstractCommand;
import com.lab7.server.commands.ConnectToDBCommand;
import com.lab7.server.commands.CountGreaterThanDistanceCommand;
import com.lab7.server.commands.CountLessThanDistanceCommand;
import com.lab7.server.commands.DisconnectCommand;
import com.lab7.server.commands.HelpCommand;
import com.lab7.server.commands.InfoCommand;
import com.lab7.server.commands.MaxByDistanceCommand;
import com.lab7.server.commands.RemoveByIdCommand;
import com.lab7.server.commands.RemoveGreaterCommand;
import com.lab7.server.commands.RemoveLowerCommand;
import com.lab7.server.commands.ShowCommand;
import com.lab7.server.commands.UpdateCommand;
import com.lab7.server.database.DBManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that execute user commands
 */
public class CommandManager {
    private static Map<String, AbstractCommand> commands;

    public CommandManager(DBManager dbManager, CollectionManager collectionManager) {
        commands = new HashMap<>();
        commands.put("add", new AddCommand(dbManager, collectionManager));
        commands.put("add_if_min", new AddIfMinCommand(dbManager, collectionManager));
        commands.put("clear", new ClearCommand(dbManager, collectionManager));
        commands.put("count_greater_than_distance", new CountGreaterThanDistanceCommand(dbManager, collectionManager));
        commands.put("count_less_than_distance", new CountLessThanDistanceCommand(dbManager, collectionManager));
        commands.put("help", new HelpCommand(dbManager));
        commands.put("info", new InfoCommand(dbManager, collectionManager));
        commands.put("max_by_distance", new MaxByDistanceCommand(dbManager, collectionManager));
        commands.put("remove_by_id", new RemoveByIdCommand(dbManager, collectionManager));
        commands.put("remove_greater", new RemoveGreaterCommand(dbManager, collectionManager));
        commands.put("remove_lower", new RemoveLowerCommand(dbManager, collectionManager));
        commands.put("show", new ShowCommand(dbManager, collectionManager));
        commands.put("update", new UpdateCommand(dbManager, collectionManager));
        commands.put("connect_to_db", new ConnectToDBCommand(dbManager));
        commands.put("disconnect_from_db", new DisconnectCommand(dbManager));
    }

    public static Map<String, AbstractCommand> getCommands() {
        return commands;
    }

    public Response executeCommand(Request request) {
        String commandName = request.getCommandName();
        AbstractCommand command = commands.get(commandName);
        return command.execute(request);
    }

//    public LoginResponse connectClient(LoginRequest loginRequest) {
//        if (dbManager.checkIfUserExist(loginRequest.getClientName())) {
//            System.out.println("Пользователь с таким именем существует");
//            if (dbManager.checkIfUserConnect(request)) {
//                connectedClients.add(loginRequest.getClientName());
//                return new LoginResponse("Клиент успешно подключился", true);
//            } else {
//                return new LoginResponse("Ошибка при вводе пароля", false);
//            }
//        }
//        System.out.println("Пользователя с таким именем не существует, пытаюсь зарегистрировать нового");
//        if (dbManager.registerNewUser(loginRequest)) {
//            return new LoginResponse("Новый клиент успешно зарегистрирован", true);
//        }
//        return new LoginResponse("Непредвиденная ошибка", false);
//    }
}
