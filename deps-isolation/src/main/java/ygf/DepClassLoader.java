package ygf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.IOUtils;
import ygf.exception.CreateDirFailedException;
import ygf.exception.DepFileNotFoundException;
import ygf.exception.ExtractJarFileException;
import ygf.exception.JarFileParseException;

/**
 * class loader for loading target jar file
 *
 * @author yanggaofeng
 * @date 20191210
 */
public class DepClassLoader extends ClassLoader {

    /**
     * dep-helper base dir
     */
    private static String BASE_DIR = "src/main/dep-helper";

    /**
     * dep-helper tmp dir
     */
    private static String TMP_DIR = BASE_DIR + File.separator + "tmp";

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
    protected Class<?> findClass(String name) {
        Class<?> cz = classCache.get(name);

        if (cz == null) {
            synchronized (this) {
                cz = classCache.get(name);
                if (cz == null) {
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

    private void preRead() {
        // 1. check jar name
        // 2. find target
        // 3. load jar file
        File file = new File(BASE_DIR + File.separator + "deps.dep");
        if (!file.exists()) {
            throw new DepFileNotFoundException("dependency files not present.");
        }


    }

    private void extractJar(File file) {
        JarFile jarFile = getJarFile(file);
        Enumeration<JarEntry> enumeration = jarFile.entries();
        while (enumeration.hasMoreElements()) {
            JarEntry entry = enumeration.nextElement();
            if (entry.isDirectory()) {
                continue;
            }

            String entryName = entry.getName();
            if (jarName.equals(entryName)) {

            }

        }
    }

    private void loadJar(File file) {

    }

    private JarFile getJarFile(File file) {
        JarFile jarFile;
        try {
            jarFile = new JarFile(file);
        } catch (IOException e) {
            throw new JarFileParseException("transfer jar file error", e);
        }

        return jarFile;
    }

    private void writeToFile(InputStream inputStream) {
        File tmpDir = createTmpDir();
        File jarFile = new File(tmpDir, jarName);
        try (FileOutputStream outputStream = new FileOutputStream(jarFile)) {
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            throw new ExtractJarFileException("extract jar file fail", e);
        }

    }

    private File createTmpDir() {
        File tmpDir = new File(TMP_DIR);

        if (!tmpDir.exists()) {
            throw new CreateDirFailedException(
                    "create src/main/dep-helper/tmp fail");
        }

        return tmpDir;

    }

}
