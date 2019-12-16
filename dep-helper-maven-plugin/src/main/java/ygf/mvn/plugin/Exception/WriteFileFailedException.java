package ygf.mvn.plugin.Exception;

public class WriteFileFailedException extends RuntimeException{
    public WriteFileFailedException(){
        super();
    }

    public WriteFileFailedException(String message){
        super(message);
    }

    public WriteFileFailedException(Throwable throwable){
        super(throwable);
    }

    public WriteFileFailedException(String message, Throwable throwable){
        super(message, throwable);
    }
}
