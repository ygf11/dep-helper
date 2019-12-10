package ygf;

import ygf.exception.DepFileNotFoundException;
import ygf.exception.JarFileParseException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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

        preRead();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    private void preRead() {
        File file = loadJarFile();
        readJar(file);
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

    private void readJar(File file) {
        JarFile jarFile = transferToJar(file);

        try {
            Enumeration<JarEntry> enumeration = jarFile.entries();
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = enumeration.nextElement();
                String name = jarEntry.getName();
                InputStream inputStream = jarFile.getInputStream(jarEntry);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int readSize;

                while ((readSize = inputStream.read(buffer)) != -1) {
                    baos.write(buffer, 0, readSize);
                }

                inputStream.close();
                bytesCache.put(name, baos.toByteArray());
            }

        } catch (IOException e) {
            bytesCache.clear();

            throw new JarFileParseException(e);
        }

    }


    private JarFile transferToJar(File file){
        JarFile jarFile;
        try{
            jarFile = new JarFile(file);
        }catch (IOException e){
            throw new JarFileParseException(e);
        }

        return jarFile;
    }
}
