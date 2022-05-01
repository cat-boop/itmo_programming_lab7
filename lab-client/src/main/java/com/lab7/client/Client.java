package com.lab7.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public final class Client {
    private static final int PORT = 1658;
    private static final int TIMEOUT = 5000;

    private Client() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите host: ");
        String host = scanner.nextLine();

        try (Socket socket = new Socket(host, PORT)) {
            socket.setSoTimeout(TIMEOUT);
            Application application = new Application(scanner, socket.getInputStream(), socket.getOutputStream());
            application.startInteractiveMode();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
