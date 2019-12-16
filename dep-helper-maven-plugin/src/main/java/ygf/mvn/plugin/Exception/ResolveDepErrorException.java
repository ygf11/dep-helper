package ygf.mvn.plugin.Exception;

public class ResolveDepErrorException extends RuntimeException {
    public ResolveDepErrorException() {
        super();
    }

    public ResolveDepErrorException(String message) {
        super(message);
    }

    public ResolveDepErrorException(Throwable throwable) {
        super(throwable);
    }

    public ResolveDepErrorException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
