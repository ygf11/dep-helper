package ygf.mvn.plugin.Exception;

public class CreateDepDirException extends RuntimeException {
    public CreateDepDirException(){
        super();
    }

    public CreateDepDirException(String message){
        super(message);
    }

    public CreateDepDirException(Throwable throwable){
        super(throwable);
    }

    public CreateDepDirException(String message, Throwable throwable){
        super(message, throwable);
    }
}
