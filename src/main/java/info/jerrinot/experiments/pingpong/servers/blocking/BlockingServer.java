package info.jerrinot.experiments.pingpong.servers.blocking;

import info.jerrinot.experiments.pingpong.servers.Server;
import info.jerrinot.experiments.pingpong.utils.Configuration;
import info.jerrinot.experiments.pingpong.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class BlockingServer implements Server {
    private static final Logger log = LoggerFactory.getLogger(BlockingServer.class);
    private final InetSocketAddress address;

    public BlockingServer(InetSocketAddress address) {
        this.address = address;
    }

    @Override
    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(address);

            for (;;) {
                Socket socket = serverSocket.accept();
                log.info("New client connected. "+socket.toString());
                new Thread(new ServerThread(socket)).start();
            }

        } catch (IOException e) {
            Utils.rethrow(e);
        }
    }
}
