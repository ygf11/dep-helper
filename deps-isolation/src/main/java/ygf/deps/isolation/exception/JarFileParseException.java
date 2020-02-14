package ygf.deps.isolation.exception;

public class JarFileParseException extends RuntimeException {
    public JarFileParseException(){
        super();
    }

    public JarFileParseException(String message){
        super(message);
    }

    public JarFileParseException(Throwable cause){
        super(cause);
    }

    public JarFileParseException(String message, Throwable cause){
        super(message, cause);
    }
}
