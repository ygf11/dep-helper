package ygf.exception;

public class CreateDirFailedException  extends RuntimeException{
    public CreateDirFailedException(){
        super();
    }

    public CreateDirFailedException(String message){
        super(message);
    }

    public CreateDirFailedException(Throwable cause){
        super(cause);
    }

    public CreateDirFailedException(String message, Throwable cause){
        super(message, cause);
    }
}
