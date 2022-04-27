package com.lab7.server;

import com.lab7.server.database.DBManager;
import com.lab7.server.logger.ServerLogger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.sql.SQLException;
import java.util.Scanner;

public final class Server {
    private static final int PORT = 1658;

    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        try {
            DBManager dbManager = new DBManager(System.getenv("DB_URL"), System.getenv("DB_USERNAME"), System.getenv("DB_PASSWORD"));
            CollectionManager collectionManager = new CollectionManager(dbManager.readElementsFromDB());
            CommandManager commandManager = new CommandManager(dbManager, collectionManager);
            ServerLogger.logInfoMessage("Подключение к базе данных установлено, ожидание подключений клиентов");

            Scanner scanner = new Scanner(System.in);
            ServerConsoleListener serverConsoleListener = new ServerConsoleListener(scanner);
            serverConsoleListener.start();

            try (Selector selector = Selector.open();
                 ServerSocketChannel socketChannel = ServerSocketChannel.open()) {

                socketChannel.socket().bind(new InetSocketAddress(PORT));
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_ACCEPT);

                Application application = new Application(commandManager, socketChannel, selector);
                application.run();

            } catch (IOException e) {
                System.out.println("Непредвиденная ошибка на сервере");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
