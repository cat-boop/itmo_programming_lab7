package com.lab7.server.client_communication;

import com.lab7.server.client_communication.ChannelState;

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

    public PackageSender(Selector selector, SocketChannel socketChannel, Map<SocketChannel, ByteBuffer> channels, Map<SocketChannel, ChannelState> channelStates) {
        this.selector = selector;
        this.socketChannel = socketChannel;
        this.channels = channels;
        this.channelStates = channelStates;
    }

    @Override
    public void run() {
        try {
            ByteBuffer buffer = channels.get(socketChannel);
            socketChannel.write(buffer);
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }

            channels.put(socketChannel, ByteBuffer.allocate(0));
            channelStates.put(socketChannel, ChannelState.READY_TO_READ);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            try {
                socketChannel.close();
            } catch (IOException a) {
                //System.out.println(a.getMessage());
            }
        }
    }
}
