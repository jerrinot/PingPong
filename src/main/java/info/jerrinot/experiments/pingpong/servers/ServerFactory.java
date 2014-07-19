package info.jerrinot.experiments.pingpong.servers;

import info.jerrinot.experiments.pingpong.PingPongException;
import info.jerrinot.experiments.pingpong.servers.blocking.BlockingServer;
import info.jerrinot.experiments.pingpong.servers.netty.NettyServer;
import info.jerrinot.experiments.pingpong.servers.nio.NioServer;
import info.jerrinot.experiments.pingpong.utils.Utils;

import java.net.InetSocketAddress;
import java.util.EnumSet;

public class ServerFactory {
    public static Server newServer(ServerType type, String address) {
        InetSocketAddress socketAddress = Utils.parseAddress(address);
        switch (type) {
            case NETTY:
                return new NettyServer(socketAddress);
            case BIO:
                return new BlockingServer(socketAddress);
            case NIO:
                return new NioServer(socketAddress);
            default:
                EnumSet<ServerType> serverTypes = EnumSet.allOf(ServerType.class);
                throw new PingPongException("Unknown server type '"+type+"', known types: "+serverTypes);
        }
    }
}
