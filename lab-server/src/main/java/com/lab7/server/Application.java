package com.lab7.server;

import com.lab7.common.util.Deserializer;
import com.lab7.common.util.Request;
import com.lab7.common.util.Response;
import com.lab7.common.util.Serializer;
import com.lab7.server.logger.ServerLogger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Application {
    private final CommandManager commandManager;
    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;
    private final Map<SocketChannel, ByteBuffer> channels;

    public Application(CommandManager commandManager, ServerSocketChannel serverSocketChannel, Selector selector) {
        this.commandManager = commandManager;
        this.serverSocketChannel = serverSocketChannel;
        this.selector = selector;
        channels = new HashMap<>();
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
                try {
                    if (key.isValid()) {
                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            channels.put(socketChannel, ByteBuffer.allocate(0));
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                        } else if (key.isReadable()) {
                            receiveRequest(key);
                        } else if (key.isWritable()) {
                            sendResponse(key);
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    key.cancel();
                }
            }
        }
    }

    private void receiveRequest(SelectionKey key) throws IOException {
        final int size = 1024;
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(size);

        int bytesRead = channel.read(buffer);
        if (bytesRead == -1) {
            throw new IOException("Клиент отключился");
        }

        ByteBuffer newBuffer = ByteBuffer.allocate(channels.get(channel).capacity() + bytesRead);
        newBuffer.put(channels.get(channel).array());
        newBuffer.put(ByteBuffer.wrap(buffer.array(), 0, bytesRead));
        channels.put(channel, newBuffer);

        Deserializer deserializer = new Deserializer(channels.get(channel).array());
        if (deserializer.possibleToDeserialize()) {
            Request request = (Request) deserializer.deserialize();
            Response response = commandManager.executeCommand(request);
            Serializer serializer = new Serializer(response);
            if (serializer.possibleToSerialize()) {
                channels.put(channel, ByteBuffer.wrap(serializer.serialize()));
            } else {
                channels.put(channel, ByteBuffer.wrap("Ошибка при сериализации ответа".getBytes()));
            }
            channel.register(selector, SelectionKey.OP_WRITE);
        } else {
            ServerLogger.logDebugMessage("Продолжаю считывать большой пакет");
        }
    }

    private void sendResponse(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = channels.get(channel);
        channel.write(buffer);
        if (buffer.hasRemaining()) {
            return;
        }
        channels.put(channel, ByteBuffer.allocate(0));
        channel.register(selector, SelectionKey.OP_READ);
    }
}
