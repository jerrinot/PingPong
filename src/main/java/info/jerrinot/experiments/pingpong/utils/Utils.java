package info.jerrinot.experiments.pingpong.utils;

import info.jerrinot.experiments.pingpong.PingPongException;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Utils {
    public static void rethrow(Throwable exception) {
        throw new PingPongException(exception);
    }

    public static void close(Closeable...closeables) {
        for (Closeable closeable : closeables) {
            close(closeable);
        }
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                rethrow(e);
            }
        }
    }

    public static InetSocketAddress parseAddress(String address) {
        String host;
        int port;
        if (address == null) {
            host = Configuration.DEFAULT_HOST;
            port = Configuration.DEFAULT_PORT;
        } else {
            address = address.trim();
            int portSplit = address.indexOf(":");
            if (portSplit == -1) {
                host = address;
                port = Configuration.DEFAULT_PORT;
            } else {
                host = address.substring(0, portSplit);
                port = Integer.parseInt(address.substring(portSplit+1));
            }
        }
        return new InetSocketAddress(host, port);
    }

    public static void exit(String message) {
        System.err.println(message);
        System.exit(-1);
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
