package ygf;

import java.util.HashMap;
import java.util.Map;

public class DepClassLoader extends ClassLoader {

    /**
     * lib path
     */
    private String path;

    /**
     * classes cache
     */
    private Map<String, Class<?>> classCache = new HashMap<String, Class<?>>();

    /**
     * byte arrays cache
     */
    private Map<String, byte[]> bytesCache = new HashMap<String, byte[]>();

    public DepClassLoader() {
        super();
    }

    public DepClassLoader(String path) {
        super();
        this.path = path;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    private Map<String, byte[]> preRead() {


        return null;
    }
}
