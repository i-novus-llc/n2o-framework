package net.n2oapp.framework.sandbox.utils;

import net.n2oapp.framework.config.register.storage.Node;
import net.n2oapp.framework.config.register.storage.PathUtil;
import net.n2oapp.framework.config.util.FileSystemUtil;
import net.n2oapp.framework.sandbox.client.model.FileModel;

import java.util.ArrayList;
import java.util.List;

import static net.n2oapp.framework.config.register.storage.PathUtil.convertPathToClasspathUri;

/**
 * Утилитный класс для работы с файлами
 */
public class FileUtil {

    /**
     * Получение файлов по пути
     * @param path Путь до файлов
     * @return     Список файлов
     */
    public static List<FileModel> findResources(String path) {
        String uri = convertPathToClasspathUri(path);
        return findFilesByUri(uri);
    }

    /**
     * Получение файлов по uri
     * @param uri uri файлов
     * @return    Список файлов
     */
    public static List<FileModel> findFilesByUri(String uri) {
        List<FileModel> files = new ArrayList<>();
        String pattern = PathUtil.convertUrlToPattern(uri, "*", "*");
        List<Node> nodes = FileSystemUtil.getNodesByLocationPattern(pattern);
        for (Node node : nodes) {
            FileModel file = new FileModel().setFile(node.getLocalPath()).setSource(node.retrieveContent());
            if (node.getLocalPath().endsWith("index.page.xml"))
                files.add(0, file);//index page is first
            else
                files.add(file);
        }
        return files;
    }
}
