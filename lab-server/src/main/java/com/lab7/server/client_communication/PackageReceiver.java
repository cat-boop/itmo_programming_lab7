package com.lab7.server.client_communication;

import com.lab7.common.util.Deserializer;
import com.lab7.server.logger.ServerLogger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Map;

public class PackageReceiver implements Runnable {
    private ByteBuffer serializedRequest;
    private volatile Selector selector;
    private final SocketChannel socketChannel;
    private volatile Map<SocketChannel, ByteBuffer> channels;
    private volatile Map<SocketChannel, ChannelState> channelStates;

    public PackageReceiver(Selector selector, SocketChannel socketChannel, Map<SocketChannel, ByteBuffer> channels, Map<SocketChannel, ChannelState> channelStates) {
        this.selector = selector;
        this.socketChannel = socketChannel;
        this.channels = channels;
        this.channelStates = channelStates;
        serializedRequest = ByteBuffer.allocate(0);
    }

    @Override
    public void run() {
        final int bufferSize = 1024;
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        try {
            int bytesRead = socketChannel.read(buffer);
            if (bytesRead == -1) {
                throw new IOException();
            }

            ByteBuffer newBuffer = ByteBuffer.allocate(serializedRequest.capacity() + bytesRead);
            newBuffer.put(serializedRequest.array());
            newBuffer.put(ByteBuffer.wrap(buffer.array(), 0, bytesRead));
            serializedRequest = ByteBuffer.wrap(newBuffer.array());

            Deserializer deserializer = new Deserializer(serializedRequest.array());
            if (deserializer.possibleToDeserialize()) {
                channels.put(socketChannel, serializedRequest);
                channelStates.put(socketChannel, ChannelState.READY_TO_WRITE);
                socketChannel.register(selector, SelectionKey.OP_WRITE);
            } else {
                channelStates.put(socketChannel, ChannelState.READY_TO_READ);
                ServerLogger.logDebugMessage("Продолжаю считывать большой пакет");
            }
        } catch (IOException e) {
            channelStates.put(socketChannel, ChannelState.ERROR);
            channels.put(socketChannel, null);
        }
    }
}
