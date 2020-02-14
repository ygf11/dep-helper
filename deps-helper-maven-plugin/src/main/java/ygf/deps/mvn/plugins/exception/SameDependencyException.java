package ygf.deps.mvn.plugins.exception;

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
