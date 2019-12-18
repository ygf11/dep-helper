package ygf;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class DepContext {
    /**
     * class loader map
     */
    private static final Map<String, ClassLoader> classLoadMap;

    static {
        classLoadMap = new ConcurrentHashMap<>();
    }

    public static Class<?> getClass(String czName, String jarName) throws ClassNotFoundException{
        Objects.requireNonNull(czName, "czName can not be null!");
        Objects.requireNonNull(jarName, "jarName can not be null! ");

        ClassLoader classLoader = classLoadMap.get(jarName);
        if (classLoader == null){
            synchronized (classLoadMap){
                classLoader = classLoadMap.get(jarName);
                if (classLoader == null){
                    classLoader = createClassLoader(jarName);
                    classLoadMap.put(jarName, classLoader);
                }
            }
        }

        return classLoader.loadClass(czName);
    }

    private static ClassLoader createClassLoader(String jarName){
        return new DepClassLoader(jarName);
    }
}
