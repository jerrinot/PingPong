package info.jerrinot.experiments.pingpong.servers.blocking;

import info.jerrinot.experiments.pingpong.utils.Configuration;
import info.jerrinot.experiments.pingpong.PingPongException;
import info.jerrinot.experiments.pingpong.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread implements Runnable {
    private final Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            int bufferSize = Configuration.WORD_SIZE;
            byte[] buffer = new byte[bufferSize];
            for (;;) {
                int pos = 0;
                do {
                    int read = inputStream.read(buffer, pos, bufferSize - pos);
                    if (pos == -1) {
                        throw new PingPongException("Unexpected end of stream.");
                    }
                    pos += read;
                } while (pos != bufferSize);

                outputStream.write(buffer);
            }
        } catch (IOException e) {
            Utils.rethrow(e);
        } finally {
            Utils.close(socket);
        }
    }
}
