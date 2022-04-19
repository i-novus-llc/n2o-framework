package net.n2oapp.framework.config.register.storage;

import net.n2oapp.framework.config.util.FileSystemUtil;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

/**
 * User: iryabov
 * Date: 21.05.13
 * Time: 20:16
 */
public class Node {

    private String name;
    private String URI;
    private String localPath;
    private File file;

    private Node() {

    }

    public static Node byAbsolutePath(String absolutePath, String configPath) {
        Node node = new Node();
        node.localPath = PathUtil.convertAbsolutePathToLocalPath(absolutePath, configPath);
        node.URI = PathUtil.convertRootPathToUrl(absolutePath);
        return node;
    }

    public static Node byLocationPattern(Resource resource, String locationPattern) throws IOException {
        Node node = new Node();
        String uri = resource.getURI().toString();
        uri = trim(uri);
        locationPattern = trim(locationPattern);

        node.localPath = calculateLocalPathByLocationPattern(locationPattern, uri);
        node.name = resource.getFilename();
        node.URI = uri;
        node.file = resource.getURI().getScheme().equals("file") ? resource.getFile() : null;
        return node;
    }


    public static Node byDirectory(File file, String dirPath) {
        Node node = new Node();
        String uri = file.toURI().toString();
        uri = trim(uri);
        dirPath = trim(dirPath);

        node.localPath = calculateLocalPathByDirectoryPath(dirPath, uri);
        node.name = file.getName();
        node.URI = uri;
        node.file = file;
        return node;
    }


    public String retrieveContent() {
        return FileSystemUtil.getContentByUri(getURI());
    }

    //есть тест
    public static String calculateLocalPathByLocationPattern(String locationPattern, String uri) {
        if (!locationPattern.contains("**")) {
            final String[] split = uri.split("/");
            return split[split.length - 1];
        }
        String tmp = locationPattern.split("\\*\\*")[0];
        tmp = tmp.replaceAll("file:", "").replaceAll("url:", "")
                .replaceAll("classpath:", "").replaceAll("classpath\\*:", "")
                .replaceAll("http:", "").replaceAll("//", "/");
        return uri.split(tmp)[1];
    }

    private static String trim(String path) {
        if (path.contains("\\"))
            path = path.replace("\\", "/");
        if (path.contains("%5C"))
            path = path.replace("%5C", "/");
        return path;
    }

    public static String calculateLocalPathByDirectoryPath(String directoryPath, String uri) {
        directoryPath = trim(directoryPath);
        uri = trim(uri);
        String res;
        try {
            res = uri.split(directoryPath)[1];
            res = trimLocalPath(res);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Error while calculating localPath by directoryPath [%s] and uri [%s]", directoryPath, uri), e);
        }
        return res;
    }

    private static String trimLocalPath(String localPath) {
        if (localPath.startsWith("/"))
            localPath = localPath.substring(1);
        return localPath;
    }


    public String getLocalPath() {
        return localPath;
    }

    public String getName() {
        return name;
    }

    public String getURI() {
        return URI;
    }


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;

        Node node = (Node) o;

        if (!URI.equals(node.URI)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return URI.hashCode();
    }

    @Override
    public String toString() {
        return "Node{" +
                "localPath='" + localPath + '\'' +
                ", absolutePath='" + URI + '\'' +
                '}';
    }
}
