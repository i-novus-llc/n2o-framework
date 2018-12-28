package net.n2oapp.framework.config.register.storage;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Утилита для удобной работы с путями
 */
public class PathUtil {

    public static String concatFileNameAndBasePath(String fileName, String basePath) {
        return basePath != null ? basePath.endsWith("/") ? basePath + fileName : basePath + "/" + fileName : fileName;
    }

    public static String convertPathToClasspathUri(String path) {
        return PathUtil.isUri(path) ? path : "classpath:" + path;
    }

    public static String convertRootPathToUrl(String rootPath) {
        String prefixFile = "file:/";
        rootPath = rootPath.replace('\\', '/');
        if (!rootPath.endsWith("/")) {
            rootPath += "/";
        }
        if (rootPath.startsWith("/"))
            rootPath = rootPath.substring(1);
        if (!isUri(rootPath)) {
            return prefixFile + rootPath;
        }
        rootPath = replacePathPatternAttribute(rootPath);
        return rootPath;
    }

    private static String replacePathPatternAttribute(String rootPath) {
        String pathPatternAttribute = "**/*.";
        rootPath = rootPath.contains(pathPatternAttribute) ? rootPath.substring(0, rootPath.indexOf(pathPatternAttribute)) : rootPath;
        return rootPath;
    }

    public static String convertUrlToAbsolutePath(String url) {
        String prefixFile = "file:";
        Boolean isOutsidePath = url.startsWith(prefixFile);
        if (!isOutsidePath) {
            return null;
        }
        url = url.substring(prefixFile.length());
        url = url.contains("*") ? url.substring(0, url.indexOf("*")) : url;
        return url;
    }

    public static String convertUrlToPattern(String url, String fileExtension) {
        return convertUrlToPattern(url, fileExtension, "*");
    }

    public static String convertUrlToPattern(String url, String fileExtension, String fileNamePattern) {
        if ((fileExtension != null) && (!url.endsWith("." + fileExtension))) {
            if (!url.endsWith("/")) {
                url += "/";
            }
            url += "**/" + fileNamePattern + "." + fileExtension;
        }
        return url;
    }

    public static String convertRootPathToFilePathPattern(String rootPath, String localPath) {
        String rootUrl = convertRootPathToUrl(rootPath);
        return concatAbsoluteAndLocalPath(rootUrl, localPath);
    }

    public static String concatAbsoluteAndLocalPath(String absolutePath, String localPath) {
        if (!absolutePath.endsWith("/"))
            absolutePath += "/";
        if (localPath.startsWith("/"))
            localPath = localPath.substring(1, localPath.length());
        return (absolutePath + localPath).replace("\\", "/");
    }

    public static Resource getContentByPathPattern(String path) {
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource resource = null;
        if (path.contains("*")) {
            try {
                Resource[] resources = resourcePatternResolver.getResources(path);
                if (resources.length != 0 && resources[0].exists())
                    resource = resources[0];
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            resource = resourcePatternResolver.getResource(path);
        }
        return resource;
    }

    public static List<String> extractDirs(String absolutePath) {
        List<String> dirs = new ArrayList<>();
        if (absolutePath == null)
            return dirs;
        File file = new File(absolutePath);
        if (file.exists())
            getDirs(absolutePath, file, dirs, false);
        return dirs;
    }

    private static void getDirs(String absolutePath, File dir, List<String> result, boolean include) {
        if (dir.isDirectory()) {
            if (include) result.add(Node.calculateLocalPathByDirectoryPath(absolutePath, dir.getAbsolutePath()));
            String[] subElements = dir.list();
            for (String subElement : subElements) {
                getDirs(absolutePath, new File(dir, subElement), result, true);
            }
        }
    }

    public static String normalize(String path) {
        if (path == null) {
            return null;
        }
        String normalized = Paths.get(path).toAbsolutePath().toString();
        if (normalized.endsWith("/"))
            normalized = normalized.substring(normalized.length() - 1);
        return normalized;
    }

    public static boolean isUri(String path) {
        return path != null && (path.startsWith("file")
                || path.startsWith("classpath")
                || path.startsWith("jar")
                || path.startsWith("war")
                || path.startsWith("ear")
                || path.startsWith("http")
                || path.startsWith("https"));
    }

    public static String convertAbsolutePathToLocalPath(String absolutePath, String configPath) {
        absolutePath = normalize(absolutePath);
        configPath = normalize(configPath);
        return absolutePath.substring(absolutePath.indexOf(configPath) + configPath.length() + 1).replace("\\", "/");
    }
}
