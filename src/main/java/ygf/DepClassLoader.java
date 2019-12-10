package ygf;

import ygf.exception.DepFileNotFoundException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DepClassLoader extends ClassLoader {

    /**
     * lib path
     */
    private String jarName;

    /**
     * classes cache
     */
    private Map<String, Class<?>> classCache = new HashMap<>();

    /**
     * byte arrays cache
     */
    private Map<String, byte[]> bytesCache = new HashMap<>();

    public DepClassLoader() {
        super();
    }

    public DepClassLoader(String jarName) {
        super();
        this.jarName = jarName;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    private Map<String, byte[]> preRead() {


        return null;
    }

    private File loadJarFile() {
        String path = "src/main/resources/dependencies/" + jarName;
        File file = new File(path);

        if (!file.exists()) {
            throw new DepFileNotFoundException(
                    "file:" + jarName + " not exists in resources dir.");
        }

        return file;
    }
}
