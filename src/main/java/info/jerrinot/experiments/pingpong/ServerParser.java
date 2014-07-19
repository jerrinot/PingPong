package info.jerrinot.experiments.pingpong;

public class ServerParser {
    private String[] args;

    public ServerParser(String args[]) {
        this.args = args;
    }

    public String getAddress() {
        return args.length < 2 ? null : args[1];
    }
}
