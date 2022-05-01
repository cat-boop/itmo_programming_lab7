package com.lab7.server.client_communication;

import com.lab7.common.util.Deserializer;
import com.lab7.common.interfaces.IResponse;
import com.lab7.common.util.Request;
import com.lab7.common.util.Serializer;
import com.lab7.server.CommandExecutor;
import com.lab7.server.CommandManager;
import com.lab7.server.ServerConsoleListener;
import com.lab7.server.logger.ServerLogger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//TODO logging
public class Application {
    private final ServerSocketChannel serverSocketChannel;
    private final CommandManager commandManager;
    private volatile Selector selector;
    private volatile Map<SocketChannel, ByteBuffer> channels;
    private volatile Map<SocketChannel, ChannelState> channelStates;

    public Application(CommandManager commandManager, ServerSocketChannel serverSocketChannel, Selector selector) {
        this.commandManager = commandManager;
        this.serverSocketChannel = serverSocketChannel;
        this.selector = selector;
        channels = Collections.synchronizedMap(new HashMap<>());
        channelStates = Collections.synchronizedMap(new HashMap<>());
    }

    public void run() throws IOException {
        System.out.println("Сервер работает");
        ServerLogger.logInfoMessage("Сервер работает");

        final long sleepTime = 100;
        while (ServerConsoleListener.isServerShouldWork()) {
            selector.select(sleepTime);
            Iterator<SelectionKey> selectionKeys = selector.selectedKeys().iterator();
            while (selectionKeys.hasNext()) {
                SelectionKey key = selectionKeys.next();
                selectionKeys.remove();
                if (key.isValid()) {
                    if (key.isAcceptable()) {
                        acceptConnection();
                    } else if (key.isReadable()) {
                        receiveRequest(key);
                    } else if (key.isWritable()) {
                        sendResponse(key);
                    }
                }
            }
        }
    }

    private void acceptConnection() throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        channels.put(socketChannel, ByteBuffer.allocate(0));
        channelStates.put(socketChannel, ChannelState.READY_TO_READ);
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    private void receiveRequest(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        if (channelStates.get(socketChannel) == ChannelState.READY_TO_READ) {
            channelStates.put(socketChannel, ChannelState.READING);
            PackageReceiver packageReceiver = new PackageReceiver(selector, socketChannel, channels, channelStates);
            new Thread(packageReceiver).start();
        }
    }

    private void sendResponse(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        if (channelStates.get(socketChannel) == ChannelState.READY_TO_WRITE) {
            channelStates.put(socketChannel, ChannelState.WRITING);
            Deserializer deserializer = new Deserializer(channels.get(socketChannel).array());
            Request request = (Request) deserializer.deserialize();
            CommandExecutor commandExecutor = new CommandExecutor(commandManager, request);
            commandExecutor.fork();
            IResponse response = commandExecutor.join();
            Serializer serializer = new Serializer(response);
            if (serializer.possibleToSerialize()) {
                channels.put(socketChannel, ByteBuffer.wrap(serializer.serialize()));
            } else {
                channels.put(socketChannel, ByteBuffer.wrap("Ошибка при сериализации ответа".getBytes()));
            }
            PackageSender packageSender = new PackageSender(selector, socketChannel, channels, channelStates);
            Thread thread = new Thread(packageSender);
            thread.start();
        }
    }
}
