package ygf.mvn.plugin.Exception;

public class SameDependencyException extends RuntimeException {
    public SameDependencyException(){
        super();
    }

    public SameDependencyException(String message){
        super(message);
    }

    public SameDependencyException(Throwable throwable){
        super(throwable);
    }

    public SameDependencyException(String message, Throwable throwable){
        super(message, throwable);
    }
}
