package ygf.exception;

public class ExtractJarFileException extends RuntimeException {
    public ExtractJarFileException(){
        super();
    }

    public ExtractJarFileException(String message){
        super(message);
    }

    public ExtractJarFileException(Throwable cause){
        super(cause);
    }

    public ExtractJarFileException(String message, Throwable cause){
        super(message, cause);
    }
}
