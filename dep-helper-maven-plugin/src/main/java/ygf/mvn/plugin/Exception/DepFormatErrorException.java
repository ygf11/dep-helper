package ygf.mvn.plugin.Exception;

public class DepFormatErrorException extends RuntimeException {
    public DepFormatErrorException(){
        super();
    }

    public DepFormatErrorException(String message){
        super(message);
    }

    public DepFormatErrorException(Throwable throwable){
        super(throwable);
    }

    public DepFormatErrorException(String message, Throwable throwable){
        super(message, throwable);
    }


}
