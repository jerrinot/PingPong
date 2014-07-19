package info.jerrinot.experiments.pingpong;

import info.jerrinot.experiments.pingpong.utils.Utils;

public class ClientParser {
    private final String[] args;

    public ClientParser(String args[]) {
        this.args = args;
    }

    public int extractNoOfThreads() {
        if (args.length < 2) {
            Utils.exit("Missing number of threads!");
        }
        return Integer.parseInt(args[1]);
    }

    public String extractAddress() {
        String address = null;
        if (args.length >= 3) {
            address = args[2];
        }
        return address;
    }

}
