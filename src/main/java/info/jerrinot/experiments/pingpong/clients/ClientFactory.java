package info.jerrinot.experiments.pingpong.clients;

import info.jerrinot.experiments.pingpong.clients.blocking.BlockingClient;
import info.jerrinot.experiments.pingpong.utils.Configuration;
import info.jerrinot.experiments.pingpong.utils.Utils;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;

public class ClientFactory {

    public static Client newClient(int threads) {
        return newClient(null, threads);
    }

    public static Client newClient(String address, int threads) {
        return newDefaultClient(address, threads);
    }

    private static Client newDefaultClient(String address, int threads) {
        InetSocketAddress socketAddress = Utils.parseAddress(address);
        return new BlockingClient(socketAddress, threads);
    }
}
