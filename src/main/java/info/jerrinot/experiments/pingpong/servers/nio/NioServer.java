package info.jerrinot.experiments.pingpong.servers.nio;

import info.jerrinot.experiments.pingpong.servers.Server;
import info.jerrinot.experiments.pingpong.utils.Configuration;
import info.jerrinot.experiments.pingpong.PingPongException;
import info.jerrinot.experiments.pingpong.utils.Utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NioServer implements Server {
    private final Selector selector;
    private final InetSocketAddress address;
    private final Map<Channel, ByteBuffer> clientMap = new HashMap<Channel, ByteBuffer>();

    public NioServer(InetSocketAddress address) {
        this.address = address;
        try {
            selector = Selector.open();
        } catch (IOException e) {
            throw new PingPongException(e);
        }
    }

    public void startServer() {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(address);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            for (;;) {
                selector.select();
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();
                    handleSelection(key);
                }
            }

        } catch (IOException e) {
            Utils.rethrow(e);
        }
    }

    private void handleSelection(SelectionKey key) throws IOException {
        if (!key.isValid()) {
            invalidate(key);
        }

        if (key.isAcceptable()) {
            accept(key);
        }

        if (key.isReadable()) {
            read(key);
        }

        if (key.isWritable()) {
            write(key);
        }
    }

    private void invalidate(SelectionKey key) {
        SelectableChannel channel = key.channel();
        clientMap.remove(channel);
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = clientMap.get(channel);
        channel.write(buffer);
        if (buffer.position() == Configuration.WORD_SIZE) {
            key.interestOps(SelectionKey.OP_READ);
            buffer.flip();
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverSocketChannel.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        clientMap.put(channel, ByteBuffer.allocateDirect(Configuration.WORD_SIZE));
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = clientMap.get(channel);
        channel.read(buffer);
        if (buffer.position() == Configuration.WORD_SIZE) {
            key.interestOps(SelectionKey.OP_WRITE);
            buffer.flip();
        }
    }
}
