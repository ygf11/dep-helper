package ygf;

import java.io.File;
import java.util.Map;
import java.util.HashMap;
import ygf.exception.DepFileNotFoundException;

/**
 * class loader for loading target jar file
 *
 * @author yanggaofeng
 * @date 20191210
 */
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

    /**
     * jars cache
     */
    private Map<String, File> jarCache = new HashMap<>();

    public DepClassLoader() {
        super();
    }

    public DepClassLoader(String jarName) {
        super();
        this.jarName = jarName;

        preRead();
    }

    @Override
    protected Class<?> findClass(String name){
        Class<?> cz = classCache.get(name);

        if (cz == null){
            synchronized (this){
                cz = classCache.get(name);
                if (cz == null){
                    byte[] bytes = bytesCache.get(name);
                    if (bytes == null) {
                        throw new DepFileNotFoundException("name:" + name + ", class file not found.");
                    }

                    Class<?> result = defineClass(name, bytes, 0, bytes.length);
                    classCache.putIfAbsent(name, result);
                    cz = result;
                }
            }
        }

        return cz;
    }

    private void preRead(){
        // 1. check jar name
        // 2. find target
        // 3. load jar file
        File file = new File("src/main/resources/deps.dep");
        if (!file.exists()){
            throw new DepFileNotFoundException("dependency files not present.");
        }
    }

    private void extractJar(){



    }

    private void loadJar(File file){

    }
}
