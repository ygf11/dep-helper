package ygf.exception;

public class ReadJarFailedException extends RuntimeException {
    public ReadJarFailedException(){
        super();
    }

    public ReadJarFailedException(String message){
        super(message);
    }

    public ReadJarFailedException(Throwable cause){
        super(cause);
    }

    public ReadJarFailedException(String message, Throwable cause){
        super(message, cause);
    }
}
