package ygf.exception;

public class DepIoException extends RuntimeException {
    public DepIoException(){
        super();
    }

    public DepIoException(String message){
        super(message);
    }

    public DepIoException(Throwable cause){
        super(cause);
    }

    public DepIoException(String message, Throwable cause){
        super(message, cause);
    }
}
