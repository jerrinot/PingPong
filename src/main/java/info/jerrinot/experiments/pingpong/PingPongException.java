package info.jerrinot.experiments.pingpong;

public class PingPongException extends RuntimeException {
    public PingPongException(String message) {
        super(message);
    }

    public PingPongException(String message, Throwable cause) {
        super(message, cause);
    }

    public PingPongException(Throwable cause) {
        super(cause);
    }
}
