package net.n2oapp.properties.io;

import org.springframework.core.io.Resource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author V. Alexeev.
 */
public class PropertiesRewriter {

    public static final String KEY_VALUE_FORMAT = "%s=%s\n";

    private static <K> boolean isTarget(String str, K key) {
        return !str.startsWith("#") && hasKey(str) && getKey(str).equals(key.toString());
    }

    private static <K, V> String updateIfNeed(String str, K key, V value) {
        return isTarget(str, key) ? String.format(KEY_VALUE_FORMAT, key.toString(), value.toString()) : str;
    }

    private static String getKey(String str) {
        return str.split("[=\\s]")[0];
    }

    private static boolean hasKey(String str) {
        return str.contains("=") || str.contains(" ");
    }

    public static <K, V> void updateProperty(Resource resource, K key, V value) throws IOException {
        if (key == null || value == null) {
            throw new IllegalStateException();
        }
        List<String> lines = readResource(resource);
        try (Writer writer = new FileWriter(createFileIfNotExists(resource), false)) {
            boolean found = false;
            for (String line : lines) {
                String tmp = updateIfNeed(line, key, value);
                writer.write(tmp);
                found = found || !tmp.equals(line);
            }
            if (!found) {
                writer.write("\n");
                writer.write(String.format(KEY_VALUE_FORMAT, key.toString(), value.toString()));
            }
            writer.flush();
        }
    }

    public static <K> void removeProperty(Resource resource, K key) throws IOException {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        List<String> lines = readResource(resource);
        try (Writer writer = new FileWriter(createFileIfNotExists(resource), false)) {
            for (String line : lines) {
                if (!isTarget(line, key)) {
                    writer.write(line);
                }
            }
            writer.flush();
        }
    }

    private static List<String> readResource(Resource resource) throws IOException {
        createFileIfNotExists(resource);
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            ArrayList<String> lines = new ArrayList<>();
            String str;
            while ((str = reader.readLine()) != null) lines.add(str.concat("\n"));
            return lines;
        }
    }

    private static File createFileIfNotExists(Resource resource) throws IOException {
        if (!resource.getFile().exists())
            createFile(resource);
        return resource.getFile();
    }

    private static void createFile(Resource resource) throws IOException {
        File file = new File(resource.getURI());
        file.getParentFile().mkdirs();
        file.createNewFile();
    }

}
