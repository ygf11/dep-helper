package ygf.exception;

public class JarNotFoundException extends RuntimeException{
    public JarNotFoundException(){
        super();
    }

    public JarNotFoundException(String message){
        super(message);
    }

    public JarNotFoundException(Throwable cause){
        super(cause);
    }

    public JarNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
