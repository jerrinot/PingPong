package info.jerrinot.experiments.pingpong;

import info.jerrinot.experiments.pingpong.clients.ClientFactory;
import info.jerrinot.experiments.pingpong.servers.ServerFactory;
import info.jerrinot.experiments.pingpong.servers.ServerType;
import info.jerrinot.experiments.pingpong.utils.Utils;

public class Main {
    private static final String MODES_HELP = "client, netty-server, nio-server, bio-server";

    public static void main(String[] args) {
        if (args.length == 0) {
            Utils.exit("Missing mode argument. Valid modes: "+MODES_HELP);
        }
        String mode = args[0];
        if ("client".equals(mode)) {
            client(args);
        } else if ("netty-server".equals(mode)) {
            startServer(ServerType.NETTY, args);
        } else if ("nio-server".equals(mode)) {
            startServer(ServerType.NIO, args);
        } else if ("bio-server".equals(mode)) {
            startServer(ServerType.BIO, args);
        } else {
            Utils.exit("Unknown mode '"+mode+"', known modes: " +MODES_HELP);
        }
    }

    private static void startServer(ServerType type, String[] args) {
        ServerParser parser = new ServerParser(args);
        String address = parser.getAddress();
        ServerFactory.newServer(type, address).startServer();
    }

    private static void client(String[] args) {
        ClientParser parser = new ClientParser(args);
        int threads = parser.extractNoOfThreads();
        String address = parser.extractAddress();
        ClientFactory.newClient(address, threads).startClient();
    }
}
