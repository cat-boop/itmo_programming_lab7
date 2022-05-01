package com.lab7.server;

import com.lab7.server.logger.ServerLogger;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerConsoleListener implements Runnable {
    private static boolean serverShouldWork;
    private final Scanner scanner;

    public ServerConsoleListener(Scanner scanner) {
        this.scanner = scanner;
        serverShouldWork = true;
    }

    @Override
    public void run() {
        while (serverShouldWork) {
            try {
                String commandName = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
                ServerLogger.logInfoMessage("На сервера введена команда \"{}\"", commandName);
                if ("exit".equals(commandName)) {
                    serverShouldWork = false;
                    ServerLogger.logInfoMessage("Работа сервера остановлена");
                }
            } catch (NoSuchElementException e) {
                serverShouldWork = false;
                ServerLogger.logInfoMessage("Работа сервера остановлена");
            }
        }
    }

    public static boolean isServerShouldWork() {
        return serverShouldWork;
    }
}
