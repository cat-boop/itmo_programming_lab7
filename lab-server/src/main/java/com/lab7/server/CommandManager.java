package com.lab7.server;

import com.lab7.server.command.AddCommand;
import com.lab7.server.command.AddIfMinCommand;
import com.lab7.server.command.ClearCommand;
import com.lab7.server.command.AbstractCommand;
import com.lab7.server.command.ConnectUserCommand;
import com.lab7.server.command.CountGreaterThanDistanceCommand;
import com.lab7.server.command.CountLessThanDistanceCommand;
import com.lab7.server.command.HelpCommand;
import com.lab7.server.command.InfoCommand;
import com.lab7.server.command.MaxByDistanceCommand;
import com.lab7.server.command.RegisterUserCommand;
import com.lab7.server.command.RemoveByIdCommand;
import com.lab7.server.command.RemoveGreaterCommand;
import com.lab7.server.command.RemoveLowerCommand;
import com.lab7.server.command.ShowCommand;
import com.lab7.server.command.UpdateCommand;
import com.lab7.server.database.DBManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Class that execute user commands
 */
public class CommandManager {
    private final Map<String, Supplier<AbstractCommand>> commands;
    private final DBManager dbManager;

    public CommandManager(DBManager dbManager, CollectionManager collectionManager) {
        this.dbManager = dbManager;
        commands = Collections.synchronizedMap(new HashMap<>());
        commands.put("add", () -> new AddCommand(dbManager, collectionManager));
        commands.put("add_if_min", () -> new AddIfMinCommand(dbManager, collectionManager));
        commands.put("clear", () -> new ClearCommand(dbManager, collectionManager));
        commands.put("count_greater_than_distance", () -> new CountGreaterThanDistanceCommand(collectionManager));
        commands.put("count_less_than_distance", () -> new CountLessThanDistanceCommand(collectionManager));
        commands.put("info", () -> new InfoCommand(collectionManager));
        commands.put("max_by_distance", () -> new MaxByDistanceCommand(collectionManager));
        commands.put("remove_by_id", () -> new RemoveByIdCommand(dbManager, collectionManager));
        commands.put("remove_greater", () -> new RemoveGreaterCommand(dbManager, collectionManager));
        commands.put("remove_lower", () -> new RemoveLowerCommand(dbManager, collectionManager));
        commands.put("show", () -> new ShowCommand(collectionManager));
        commands.put("update", () -> new UpdateCommand(dbManager, collectionManager));
        commands.put("connect_user", () -> new ConnectUserCommand(dbManager));
        commands.put("register_user", () -> new RegisterUserCommand(dbManager));
        commands.put("help", () -> new HelpCommand(this));
    }

    public Map<String, Supplier<AbstractCommand>> getCommands() {
        return commands;
    }

    public AbstractCommand getCommandByName(String name) {
        return commands.get(name).get();
    }

    public DBManager getDbManager() {
        return dbManager;
    }
}
