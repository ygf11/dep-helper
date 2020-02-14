package ygf.deps.isolation.exception;

public class DepFileNotFoundException extends RuntimeException {
    public DepFileNotFoundException(){
        super();
    }

    public DepFileNotFoundException(String message){
        super(message);
    }

    public DepFileNotFoundException(Throwable cause){
        super(cause);
    }

    public DepFileNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
