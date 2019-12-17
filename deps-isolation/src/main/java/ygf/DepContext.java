package ygf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DepContext {
    /**
     * class loader map
     */
    private static final Map<String, DepClassLoader> classLoadMap;

    static {
        classLoadMap = new ConcurrentHashMap<>();
    }



}
