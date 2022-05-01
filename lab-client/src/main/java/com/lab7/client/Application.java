package com.lab7.client;

import com.lab7.common.util.ConnectResponse;
import com.lab7.client.client_exceptions.ReadElementFromScriptException;
import com.lab7.client.client_exceptions.RecursiveScriptException;
import com.lab7.common.util.Deserializer;
import com.lab7.common.util.Request;
import com.lab7.common.util.CommandResponse;
import com.lab7.common.util.Serializer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Application {
    private final Scanner consoleScanner;
    private final OutputStream outputStream;
    private final InputStream inputStream;
    private final Set<String> scriptNames;

    public Application(Scanner consoleScanner, InputStream inputStream, OutputStream outputStream) {
        this.consoleScanner = consoleScanner;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        scriptNames = new HashSet<>();
    }

    public void startInteractiveMode() throws IOException {
        RouteReader consoleRouteReader = new RouteReader(consoleScanner, false);

        while (true) {
            System.out.print("Вы хотите зарегистрироваться (1) или войти под существующим пользователем (2) ?: ");
            String answer = consoleScanner.nextLine();
            if ("1".equals(answer)) {
                dbRequest("register_user");
                break;
            }
            if ("2".equals(answer)) {
                dbRequest("connect_user");
                break;
            }
        }

        boolean clientAlive;
        do {
            System.out.print("Введите команду: ");
            String command = consoleScanner.nextLine();
            clientAlive = executeCommand(command, consoleRouteReader);
        } while (clientAlive);
        System.out.print("Отключился от сервера");
    }

    private boolean executeCommand(String command, RouteReader routeReader) throws IOException {
        try {
            CommandAnalyzer commandAnalyzer = new CommandAnalyzer(command);
            if (commandAnalyzer.commandIsExit()) {
                return false;
            }
            if (commandAnalyzer.isDBCommand()) {
                dbRequest(commandAnalyzer.getCommandName());
            } else if (commandAnalyzer.commandIsScript()) {
                return executeScript(commandAnalyzer.getCommandArgument());
            } else {
                sendRequest(RequestCreator.createRequest(command, routeReader));
                CommandResponse response = (CommandResponse) receiveResponse();
                System.out.println(response);
            }
        } catch (IllegalStateException | RecursiveScriptException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    private void dbRequest(String dbRequestName) throws IOException {
        boolean clientConnected = false;
        do {
            System.out.print("Введите имя пользователя: ");
            String clientName = consoleScanner.nextLine();
            if (clientName.isEmpty()) {
                System.out.println("Имя клиента не может быть пустым");
                continue;
            }
            System.out.print("Введите пароль: ");
            String clientPassword = consoleScanner.nextLine();
            if (clientPassword.isEmpty()) {
                System.out.println("Пароль не может быть пустым");
                continue;
            }
            Request request = new Request(dbRequestName, clientName, clientPassword);
            sendRequest(request);
            ConnectResponse response = (ConnectResponse) receiveResponse();
            if (response.isClientConnected()) {
                clientConnected = true;
                RequestCreator.setClientName(clientName);
                RequestCreator.setClientPassword(clientPassword);
            }
            System.out.println(response.getConnectMessage());
        } while (!clientConnected);
    }

    private boolean executeScript(String scriptName) throws IOException {
        try {
            File file = new File(scriptName);
            Scanner scannerToScript = new Scanner(file);
            RouteReader scriptRouteReader = new RouteReader(scannerToScript, true);

            if (scriptNames.contains(scriptName)) {
                throw new RecursiveScriptException("Скрипты нельзя вызывать рекурсивно");
            }
            scriptNames.add(scriptName);

            System.out.println("Исполнение скрипта \"" + scriptName + "\" начато");
            while (scannerToScript.hasNextLine()) {
                String scriptCommand = scannerToScript.nextLine();
                if (!executeCommand(scriptCommand, scriptRouteReader)) {
                    return false;
                }
            }
            System.out.println("Исполнение скрипта \"" + scriptName + "\" завершено");
            scriptNames.remove(scriptName);
        } catch (FileNotFoundException | ReadElementFromScriptException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    private void sendRequest(Request objectToSend) throws IOException {
        Serializer serializer = new Serializer(objectToSend);
        if (serializer.possibleToSerialize()) {
            outputStream.write(serializer.serialize());
        } else {
            System.out.println("Невозможно сериализовать запрос");
        }
    }

    private Serializable receiveResponse() throws IOException {
        final int startBufferSize = 1024;
        ByteBuffer mainBuffer = ByteBuffer.allocate(0);
        while (true) {
            byte[] bytesToDeserialize = new byte[startBufferSize];
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            int bytesCount = bis.read(bytesToDeserialize);
            ByteBuffer newBuffer = ByteBuffer.allocate(mainBuffer.capacity() + bytesCount);
            newBuffer.put(mainBuffer);
            newBuffer.put(ByteBuffer.wrap(bytesToDeserialize, 0, bytesCount));
            mainBuffer = ByteBuffer.wrap(newBuffer.array());

            Deserializer deserializer = new Deserializer(mainBuffer.array());
            if (deserializer.possibleToDeserialize()) {
                return deserializer.deserialize();
            } else {
                List<ByteBuffer> buffers = new ArrayList<>();
                int bytesLeft = bis.available();
                int len = bytesLeft;
                while (bytesLeft > 0) {
                    byte[] leftBytesToSerialize = new byte[bytesLeft];
                    if (bis.read(leftBytesToSerialize) == -1) {
                        throw new IOException("Сервер не работает");
                    }
                    buffers.add(ByteBuffer.wrap(leftBytesToSerialize));
                    bytesLeft = bis.available();
                    len += bytesLeft;
                }
                newBuffer = ByteBuffer.allocate(len + mainBuffer.capacity());
                newBuffer.put(mainBuffer);
                buffers.forEach(newBuffer::put);
                mainBuffer = ByteBuffer.wrap(newBuffer.array());

                deserializer = new Deserializer(mainBuffer.array());
                if (deserializer.possibleToDeserialize()) {
                    return deserializer.deserialize();
                }
            }
        }
    }
}
