package ygf.exception;

public class LoadClassException extends RuntimeException {
    public LoadClassException(){
        super();
    }

    public LoadClassException(String message){
        super(message);
    }

    public LoadClassException(Throwable cause){
        super(cause);
    }

    public LoadClassException(String message, Throwable cause){
        super(message, cause);
    }
}
