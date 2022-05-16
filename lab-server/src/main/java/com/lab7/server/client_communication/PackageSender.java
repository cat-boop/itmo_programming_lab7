package com.lab7.server.client_communication;

import com.lab7.common.interfaces.IResponse;
import com.lab7.common.util.Serializer;
import com.lab7.server.CommandExecutor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Map;

public class PackageSender implements Runnable {
    private volatile Selector selector;
    private final SocketChannel socketChannel;
    private volatile Map<SocketChannel, ByteBuffer> channels;
    private volatile Map<SocketChannel, ChannelState> channelStates;
    private final CommandExecutor commandExecutor;

    public PackageSender(Selector selector, SocketChannel socketChannel, Map<SocketChannel, ByteBuffer> channels, Map<SocketChannel, ChannelState> channelStates, CommandExecutor commandExecutor) {
        this.selector = selector;
        this.socketChannel = socketChannel;
        this.channels = channels;
        this.channelStates = channelStates;
        this.commandExecutor = commandExecutor;
    }

    @Override
    public void run() {
        try {
            commandExecutor.fork();
            IResponse response = commandExecutor.join();
            Serializer serializer = new Serializer(response);
            ByteBuffer buffer = (serializer.possibleToSerialize() ? ByteBuffer.wrap(serializer.serialize()) : ByteBuffer.wrap("Ошибка при сериализации ответа".getBytes()));
            socketChannel.write(buffer);
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }

            channels.put(socketChannel, ByteBuffer.allocate(0));
            channelStates.put(socketChannel, ChannelState.READY_TO_READ);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            channelStates.put(socketChannel, ChannelState.ERROR);
            channels.put(socketChannel, null);
        }
    }
}
