package info.jerrinot.experiments.pingpong.clients.blocking;

import info.jerrinot.experiments.pingpong.clients.Client;
import info.jerrinot.experiments.pingpong.utils.Configuration;
import info.jerrinot.experiments.pingpong.utils.Utils;

import java.net.InetSocketAddress;

public class BlockingClient implements Client {
    private final ClientThread[] clients;
    private final int noOfThreads;
    private final InetSocketAddress address;

    public BlockingClient(InetSocketAddress address, int noOfThreads) {
        this.address = address;
        this.noOfThreads = noOfThreads;
        clients = new ClientThread[noOfThreads];
    }

    @Override
    public void startClient()  {
        for (int i = 0; i < noOfThreads; i++) {
            ClientThread client = new ClientThread(address);
            new Thread(client).start();
            clients[i] = client;
        }

        //warm-up
        Utils.sleep(Configuration.CLIENT_WARM_UP);

        new MonitorThread().start();
    }

    private class MonitorThread extends Thread {
        @Override
        public void run() {
//            setDaemon(true);
            getCount();
            int sleepingTimeMs = Configuration.MONITORING_FREQ * 1000;

            for (;;) {
                try {
                    Thread.sleep(sleepingTimeMs);
                } catch (InterruptedException e) {
                    Utils.rethrow(e);
                }
                int totalOps = getCount() / Configuration.MONITORING_FREQ;
                int threadOps = totalOps / noOfThreads;
                System.out.println("Total Operation/second: "+totalOps);
                System.out.println("Operation per Thread/second: "+threadOps);
            }
        }

        private int getCount() {
            int count = 0;
            for (ClientThread client: clients) {
                count += client.getAndReset();
            }
            return count;
        }
    }
}
