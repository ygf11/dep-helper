package ygf;

import java.io.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.IOUtils;
import ygf.exception.*;

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
    private static String BASE_DIR = "dep-helper";

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
                    byte[] bytes = getByteArray(name);
                    if (bytes == null) {
                        throw new DepFileNotFoundException(name + ", class file not found.");
                    }

                    Class<?> result = defineClass(name, bytes, 0, bytes.length);
                    classCache.putIfAbsent(name, result);
                    cz = result;
                }
            }
        }

        return cz;
    }

    private byte[] getByteArray(String czName) {
        if (czName == null) {
            throw new LoadClassException("czName is null");
        }

        byte[] bytes = bytesCache.get(czName);
        if (bytes != null) {
            return bytes;
        }

        String jarPath = TMP_DIR + File.separator + jarName;
        File jar = new File(jarPath);
        JarFile jarFile = getJarFile(jar);

        Enumeration<JarEntry> enumeration = jarFile.entries();
        while (enumeration.hasMoreElements()) {
            JarEntry entry = enumeration.nextElement();

            if (entry.isDirectory()) {
                continue;
            }

            String entryName = entry.getName().replace(".class", "")
                    .replaceAll("/", ".");

            if (czName.equals(entryName)) {
                try (InputStream inputStream = jarFile.getInputStream(entry)) {
                    bytes = readBytes(inputStream);
                    bytesCache.putIfAbsent(czName, bytes);
                } catch (IOException e) {
                    throw new ExtractJarFileException("get jar input stream failed", e);
                }

                break;
            }
        }

        return bytes;
    }

    private void preRead() {
        // 1. check jar name
        // 2. find target
        // 3. load jar file
        File file = new File(BASE_DIR + File.separator + "deps.jar");
        if (!file.exists()) {
            throw new DepFileNotFoundException("dependency files not present.");
        }

        extractJar(file);
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
                try (InputStream inputStream = jarFile.getInputStream(entry)) {
                    writeToFile(inputStream);
                } catch (IOException e) {
                    throw new ExtractJarFileException("get jar input stream failed", e);
                }
            }

        }
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
            boolean success = tmpDir.mkdir();
            if (!success) {
                throw new CreateDirFailedException(
                        "create src/main/dep-helper/tmp fail");
            }
        }

        return tmpDir;
    }

    private byte[] readBytes(InputStream inputStream) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            IOUtils.copy(inputStream, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new ReadJarFailedException("read jar file error", e);
        }
    }

}
