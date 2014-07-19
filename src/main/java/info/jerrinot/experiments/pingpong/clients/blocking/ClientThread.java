package info.jerrinot.experiments.pingpong.clients.blocking;

import info.jerrinot.experiments.pingpong.utils.Configuration;
import info.jerrinot.experiments.pingpong.PingPongException;
import info.jerrinot.experiments.pingpong.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientThread implements Runnable {
    private static final Random rand = new Random();

    private final byte[] buffer = new byte[Configuration.WORD_SIZE];
    private final InetSocketAddress address;
    private final AtomicInteger count = new AtomicInteger();

    public ClientThread(InetSocketAddress address) {
        this.address = address;
        rand.nextBytes(buffer);
    }

    public int getAndReset() {
        return count.getAndSet(0);
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        Socket socket = new Socket();
        try {
            socket.connect(address);

            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            for (;;) {
                outputStream.write(buffer);

                int pos = 0;
                int bufferSize = Configuration.WORD_SIZE;
                do {
                    int read = inputStream.read(buffer, pos, bufferSize - pos);
                    if (pos == -1) {
                        throw new PingPongException("Unexpected end of stream.");
                    }
                    pos += read;
                } while (pos != bufferSize);
                count.incrementAndGet();
            }
        } catch (IOException e) {
            Utils.rethrow(e);
        } finally {
            Utils.close(inputStream, outputStream, socket);
        }
    }
}
